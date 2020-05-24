package MySpringMVC.V2.core.annotation.utils;

import java.lang.reflect.Method;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2019-03-27 10:04 AM
 */
public final class ReflectionUtils {

    /**
     * 调用无参数方法
     *
     * @param method 方法对象
     * @param target 调用对象
     * @return 执行结果
     */
    public static Object invokeMethod(Method method, Object target) {
        return invokeMethod(method, target, new Object[0]);
    }

    /**
     * 调用指定对象的方法
     *
     * @param method 方法对象
     * @param target 调用对象
     * @param args   方法参数
     * @return 执行结果
     */
    public static Object invokeMethod(Method method, Object target, Object... args) {
        try {
            method.setAccessible(true);
            return method.invoke(target, args);
        } catch (Exception ex) {
            throw new IllegalStateException(String.format("执行%s.%s()方法错误!"
                    , target.getClass().getName(), method.getName()), ex);
        }
    }

    /**
     * 是否为equals方法
     *
     * @see Object#equals(Object)
     */
    public static boolean isEqualsMethod(Method method) {
        if (!"equals".equals(method.getName())) {
            return false;
        }
        Class<?>[] paramTypes = method.getParameterTypes();
        return (paramTypes.length == 1 && paramTypes[0] == Object.class);
    }

    /**
     * 是否为hashCode方法
     *
     * @see Object#hashCode()
     */
    public static boolean isHashCodeMethod(Method method) {
        return "hashCode".equals(method.getName()) && method.getParameterCount() == 0;
    }

    /**
     * 是否为Object的toString方法
     *
     * @see Object#toString()
     */
    public static boolean isToStringMethod(Method method) {
        return "toString".equals(method.getName()) && method.getParameterCount() == 0;
    }
}
