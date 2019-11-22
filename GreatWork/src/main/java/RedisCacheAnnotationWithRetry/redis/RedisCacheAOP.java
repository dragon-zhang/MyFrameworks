package RedisCacheAnnotationWithRetry.redis;

import RedisCacheAnnotationWithRetry.reflect.ReflectUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.retry.annotation.AnnotationAwareRetryOperationsInterceptor;
import org.springframework.retry.annotation.RetryConfiguration;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Objects;

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

    private final RedisUtil redisUtil;

    @Pointcut("@annotation(RedisCacheAnnotationWithRetry.redis.RedisCache)")
    public void annotationPointCut() {
        //do nothing, for aop purpose
    }

    /**
     * 这里AOP的@Retryable必须存在，不然无法使用重试机制
     */
    @Retryable
    @Around(value = "annotationPointCut()")
    public Object cache(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Method method = ReflectUtil.getMethod(proceedingJoinPoint);
        RedisCache redisCache = method.getAnnotation(RedisCache.class);
        Object[] args = proceedingJoinPoint.getArgs();
        String hashKey = generateHashKey(args);
        Object mappedResult = null;
        try {
            Object result = redisUtil.getCache(redisCache.key(), hashKey);
            mappedResult = ReflectUtil.getResult(result, method);
        } catch (Exception e) {
            log.error("redis request failed !", e);
        }
        if (Objects.isNull(mappedResult)) {
            final String mutexKey = redisCache.key() + "_lock";
            if (redisUtil.unlock(mutexKey)) {
                //设置成1秒过期
                redisUtil.expireInOneSecend(mutexKey);
                mappedResult = ReflectUtil.getResult(proceedingJoinPoint.proceed(args), method);
                redisUtil.putAndExpire(redisCache.key(), hashKey, mappedResult, redisCache.expire(), redisCache.timeUnit());
                redisUtil.delete(mutexKey);
            } else {
                log.error("Cache specified key{" + hashKey + "} hit missed or it had been locked");
                Retryable retryable = method.getAnnotation(Retryable.class);
                if (retryable != null) {
                    //触发重试机制
                    throw new RuntimeException("Cache specified key{" + hashKey + "} hit missed or it had been locked");
                }
            }
        }
        return mappedResult;
    }

    private String generateHashKey(Object[] args) {
        StringBuilder hashKey = new StringBuilder();
        for (int i = 0; i < args.length - 1; i++) {
            hashKey.append((String) args[i]).append("-");
        }
        if (args.length > 0) {
            hashKey.append((String) args[args.length - 1]);
        }
        return hashKey.toString();
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        //获取真正执行重试逻辑的方法，并通过反射调用
        AnnotationAwareRetryOperationsInterceptor target = ((AnnotationAwareRetryOperationsInterceptor) retryConfiguration.getAdvice());
        Method method = target.getClass().getDeclaredMethod("getDelegate", Object.class, Method.class);
        method.setAccessible(true);
        Method targetMethod = this.getClass().getDeclaredMethod("cache", ProceedingJoinPoint.class);
        Retryable fake = targetMethod.getAnnotation(Retryable.class);
        Map<String, Object> fakeMemberValues = ReflectUtil.getAnnotationMemberValues(fake);
        Retryable real = invocation.getMethod().getAnnotation(Retryable.class);
        if (real != null) {
            //运行时修改注解字段的属性
            Map<String, Object> realMemberValues = ReflectUtil.getAnnotationMemberValues(real);
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
}
