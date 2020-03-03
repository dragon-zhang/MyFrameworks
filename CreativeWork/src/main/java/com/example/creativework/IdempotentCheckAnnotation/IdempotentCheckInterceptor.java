package com.example.creativework.IdempotentCheckAnnotation;

import com.alibaba.fastjson.JSON;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.core.RedisTemplate;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>文件名称：com.vdian.vclub.vtoolkit.commons.interceptor.IdempotentCheckInterceptor</p>
 * <p>文件描述：拦截需要进行幂等性校验的方法</p>
 * <p>版权所有： (C)2011-2099 微店</p>
 * <p>内容摘要： </p>
 * <p>其他说明： </p>
 * <p>完成日期：2020/2/6 16:14 PM</p>
 *
 * @author SuccessZhang
 * @version 1.0
 */
@Slf4j
@Aspect
@RequiredArgsConstructor
public class IdempotentCheckInterceptor {

    private static final char COMMA = ',';

    private final ApplicationContext applicationContext;

    private final RedisTemplate redisTemplate;

    private final Map<String, Method> methodCache = new HashMap<>();

    private final Map<Method, Parameter[]> parameterCache = new HashMap<>();

    @Pointcut("@annotation(com.example.creativework.IdempotentCheckAnnotation.IdeCheck)")
    public void idempotentCheck() {
    }

    @SuppressWarnings("unchecked")
    @Around("idempotentCheck()")
    public Object around(ProceedingJoinPoint point) {
        Object result = null;
        Object[] args = point.getArgs();
        try {
            Class<?> clazz = getTargetClass(point);
            Method method = getRealMethod(point, clazz);
            IdeCheck ideCheck = method.getAnnotation(IdeCheck.class);
            String key = generateKey(clazz, method, args);
            result = JSON.parseObject(String.valueOf(redisTemplate.opsForValue().get(key)), method.getGenericReturnType());
            Adjudicator adjudicator = applicationContext.getBean(ideCheck.adjudicator());
            if (adjudicator.judgeRedisResult(result)) {
                log.info("idempotent results returned:{}", result);
                return result;
            }
            result = point.proceed(args);
            if (adjudicator.judgeMethodResult(result)) {
                redisTemplate.opsForValue().set(key, JSON.toJSONString(result), ideCheck.seconds(), ideCheck.timeUnit());
            }
        } catch (Throwable t) {
            log.error(t.getMessage(), t);
        }
        return result;
    }

    private String generateKey(Class<?> clazz, Method method, Object[] args) {
        StringBuilder sb = new StringBuilder(clazz.getName());
        sb.append(".").append(method.getName()).append("(");
        Parameter[] parameters = parameterCache.get(method);
        if (parameters == null) {
            parameters = method.getParameters();
            parameterCache.put(method, parameters);
        }
        for (int i = 0; i < parameters.length; i++) {
            if (!parameters[i].isAnnotationPresent(CheckParam.class)) {
                continue;
            }
            sb.append(args[i].toString());
            sb.append(",");
        }
        int last = sb.length() - 1;
        if (sb.charAt(last) == COMMA) {
            sb.deleteCharAt(last);
        }
        sb.append(") returns ");
        sb.append(method.getGenericReturnType());
        return sb.toString();
    }

    private Method getRealMethod(ProceedingJoinPoint point, Class<?> clazz) throws NoSuchMethodException {
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        String methodName = signature.getName();
        String key = generateMethodKey(clazz, method, methodName);
        Method result = methodCache.get(key);
        if (result == null) {
            result = clazz.getDeclaredMethod(methodName, method.getParameterTypes());
            methodCache.put(key, result);
        }
        return result;
    }

    private String generateMethodKey(Class<?> clazz, Method method, String methodName) {
        StringBuilder key = new StringBuilder(clazz.getName());
        key.append(".").append(methodName).append("(");
        for (Class<?> parameterType : method.getParameterTypes()) {
            key.append(parameterType.toString());
            key.append(",");
        }
        int last = key.length() - 1;
        if (key.charAt(last) == COMMA) {
            key.deleteCharAt(last);
        }
        key.append(") returns ");
        key.append(method.getGenericReturnType());
        return key.toString();
    }

    private Class<?> getTargetClass(ProceedingJoinPoint point) {
        return point.getTarget().getClass();
    }

}
