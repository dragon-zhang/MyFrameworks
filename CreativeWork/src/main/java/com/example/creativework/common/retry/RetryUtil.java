package com.example.creativework.common.retry;

import org.springframework.context.annotation.Configuration;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * @author SuccessZhang
 */
@SuppressWarnings("unchecked")
@Component
@EnableRetry
@Configuration
public class RetryUtil {

    private static Map<String, Object> annotationMemberValues;

    static {
        try {
            Retryable annotation = RetryUtil.class
                    .getDeclaredMethod("retry", Callable.class)
                    .getAnnotation(Retryable.class);
            InvocationHandler invocationHandler = Proxy.getInvocationHandler(annotation);
            Field values = invocationHandler.getClass().getDeclaredField("memberValues");
            values.setAccessible(true);
            annotationMemberValues = (Map<String, Object>) values.get(invocationHandler);
        } catch (Exception ignored) {
        }
    }

    @Retryable
    public <T> T retry(Callable<T> callable) throws Exception {
        return callable.call();
    }
}
