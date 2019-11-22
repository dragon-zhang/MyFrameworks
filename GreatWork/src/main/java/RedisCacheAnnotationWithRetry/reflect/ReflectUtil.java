package RedisCacheAnnotationWithRetry.reflect;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Collection;
import java.util.Map;

/**
 * @author SuccessZhang
 */
@Slf4j
@SuppressWarnings({"unchecked", "unused"})
public class ReflectUtil {

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
    public static Map<String, Object> getAnnotationMemberValues(Annotation annotation) throws Exception {
        InvocationHandler invocationHandler = Proxy.getInvocationHandler(annotation);
        Field values = invocationHandler.getClass().getDeclaredField("memberValues");
        values.setAccessible(true);
        return (Map<String, Object>) values.get(invocationHandler);
    }

    public static Method getMethod(ProceedingJoinPoint joinPoint) {
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

    public static Object getResult(Object data, Method method) throws ClassNotFoundException {
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

    private static String getTypeName(Method method) {
        String genericReturnType = method.getGenericReturnType().getTypeName();
        return genericReturnType.substring(genericReturnType.indexOf("<") + 1, genericReturnType.lastIndexOf(">"));
    }
}
