package MySpringBoot.redis;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.retry.annotation.AnnotationAwareRetryOperationsInterceptor;
import org.springframework.retry.annotation.RetryConfiguration;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author SuccessZhang
 * 可与Spring的@Retryable混合使用，
 * 使用时@Retryable配置在实际目标方法中，
 * 且interceptor属性必须指定为"redisCacheAOP"，
 * 如@Retryable(interceptor = "redisCacheAOP", maxAttempts = 4)
 */
@Component
@Slf4j
@Aspect
@RequiredArgsConstructor
public class RedisCacheAOP implements MethodInterceptor {

    private final RetryConfiguration retryConfiguration;

    private final RedisTemplate redisTemplate;

    @Pointcut("@annotation(MySpringBoot.redis.RedisCache)")
    public void annotationPointCut() {
        //do nothing, for aop purpose
    }

    /**
     * 这里AOP的@Retryable必须存在，不然无法使用重试机制
     */
    @SuppressWarnings("unchecked")
    @Retryable
    @Around(value = "annotationPointCut()")
    public Object cache(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Method method = getMethod(proceedingJoinPoint);
        RedisCache redisCache = method.getAnnotation(RedisCache.class);
        Retryable retryable = method.getAnnotation(Retryable.class);
        Object[] args = proceedingJoinPoint.getArgs();
        String app = (String) args[0];
        Object result = null;
        try {
            result = getResult(redisTemplate.opsForHash().get(redisCache.key(), app), method);
        } catch (Exception e) {
            log.error("redis request failed !", e);
        }
        if (Objects.isNull(result)) {
            final String mutexKey = redisCache.key() + "_lock";
            boolean unlocked = false;
            try {
                unlocked = (boolean) redisTemplate.execute((RedisCallback) connection -> {
                    //只在键key不存在的情况下，将键key的值设置为value,若键key已经存在，则 SETNX 命令不做任何动作
                    //命令在设置成功时返回1，设置失败时返回0
                    return connection.setNX(mutexKey.getBytes(), "flag".getBytes());
                });
            } catch (Exception e) {
                log.error("redis request failed !", e);
            }
            if (unlocked) {
                //设置成1秒过期
                redisTemplate.expire(mutexKey, 1000, TimeUnit.MILLISECONDS);
                result = getResult(proceedingJoinPoint.proceed(args), method);
                redisTemplate.opsForHash().put(redisCache.key(), app, result);
                redisTemplate.expire(redisCache.key(), redisCache.expire(), redisCache.timeUnit());
                redisTemplate.delete(mutexKey);
            } else {
                log.error("Cache specified key{" + app + "} hit missed or it had been locked");
                if (retryable != null) {
                    //触发重试机制
                    throw new RuntimeException("Cache specified key{" + app + "} hit missed or it had been locked");
                }
            }
        }
        return result;
    }

    private Method getMethod(ProceedingJoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        if (method.getDeclaringClass().isInterface()) {
            try {
                method = joinPoint.getTarget()
                        .getClass()
                        .getDeclaredMethod(signature.getName(), method.getParameterTypes());
            } catch (NoSuchMethodException e) {
                log.error("NoSuchMethodException when get annotation method " + method.getDeclaringClass()
                        .getName() + "#" + method.getName(), e);
            }
        }
        return method;
    }

    private Object getResult(Object data, Method method) throws ClassNotFoundException {
        Object result;
        Class<?> returnType = method.getReturnType();
        if (Collection.class.isAssignableFrom(returnType)) {
            //返回值是集合类型
            result = JSONObject.parseArray(JSON.toJSONString(data),
                    Class.forName(getTypeName(method)));
        } else if (Map.class.isAssignableFrom(returnType)) {
            //返回值是Map类型
            result = JSONObject.parseObject(JSON.toJSONString(data), Map.class);
        } else {
            //返回值是普通JavaBean类型
            result = JSONObject.parseObject(JSON.toJSONString(data), returnType);
        }
        return result;
    }

    private String getTypeName(Method method) {
        String genericReturnType = method.getGenericReturnType().getTypeName();
        return genericReturnType.substring(genericReturnType.indexOf("<") + 1, genericReturnType.lastIndexOf(">"));
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        //获取真正执行重试逻辑的方法，并通过反射调用
        AnnotationAwareRetryOperationsInterceptor target = ((AnnotationAwareRetryOperationsInterceptor) retryConfiguration.getAdvice());
        Method method = target.getClass().getDeclaredMethod("getDelegate", Object.class, Method.class);
        method.setAccessible(true);
        Method targetMethod = this.getClass().getDeclaredMethod("cache", ProceedingJoinPoint.class);
        Retryable fake = targetMethod.getAnnotation(Retryable.class);
        Map<String, Object> fakeMemberValues = getAnnotationMemberValues(fake);
        Retryable real = invocation.getMethod().getAnnotation(Retryable.class);
        if (real != null) {
            //运行时修改注解字段的属性
            Map<String, Object> realMemberValues = getAnnotationMemberValues(real);
            for (String key : fakeMemberValues.keySet()) {
                fakeMemberValues.put(key, realMemberValues.get(key));
            }
            fakeMemberValues.put("interceptor", "");
        } else {
            //代理的方法没有加@Retryable，所以AOP也不采用重试策略，运行时修改注解maxAttempts属性为0
            fakeMemberValues.put("maxAttempts", "0");
        }
        MethodInterceptor delegate = (MethodInterceptor) method.invoke(target, invocation.getThis(), targetMethod);
        if (delegate != null) {
            return delegate.invoke(invocation);
        } else {
            return invocation.proceed();
        }
    }

    /**
     * 获取注解的字段属性
     * 如public @interface RedisCache {
     * String key();
     * int expire() default 10 * 60;
     * TimeUnit timeUnit() default TimeUnit.SECONDS;
     * }
     * 获取到的Map包含key：key,expire,timeUnit；
     * 值则是注解所配置的对应key名称方法的值
     *
     * @param annotation 注解实例
     * @return 注解K-V字段Map
     */
    @SuppressWarnings("unchecked")
    private Map<String, Object> getAnnotationMemberValues(Annotation annotation) throws Exception {
        InvocationHandler invocationHandler = Proxy.getInvocationHandler(annotation);
        Field values = invocationHandler.getClass().getDeclaredField("memberValues");
        values.setAccessible(true);
        return (Map<String, Object>) values.get(invocationHandler);
    }
}
