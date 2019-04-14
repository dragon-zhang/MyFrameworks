package MyDynamicProxy.test;

import MyDynamicProxy.MyInvocationHandler;
import MyDynamicProxy.MyProxy;
import MySpringMVC.V2.aop.AOPMethods;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * @author SuccessZhang
 */
public enum TestProxy implements MyInvocationHandler {

    //枚举单例
    INSTANCE;

    private static Object target;

    @SuppressWarnings("unchecked")
    public static <T> T getInstance(Class<T> type) {
        try {
            target = type.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        if (AOPMethods.class.isAssignableFrom(type)) {
            return (T) MyProxy.jdkNewProxyInstance(type.getInterfaces(), INSTANCE);
        } else if (!Modifier.toString(type.getModifiers()).contains("final")) {
            return (T) MyProxy.cglibNewProxyInstance(type, INSTANCE);
        }
        throw new RuntimeException("the " + type + " should implements " + AOPMethods.class + ",or the modifiers should not contains 'final' !");
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object result = null;
        Class<?> clazz = target.getClass();
        try {
            clazz.getDeclaredMethod("aroundBefore").invoke(target);
            clazz.getDeclaredMethod("before").invoke(target);
            result = method.invoke(target, args);
            clazz.getDeclaredMethod("aroundAfter").invoke(target);
            clazz.getDeclaredMethod("afterReturning", Object.class).invoke(target, result);
        } catch (Throwable throwable) {
            clazz.getDeclaredMethod("afterThrowing", Throwable.class).invoke(target, throwable);
            throwable.printStackTrace();
        } finally {
            clazz.getDeclaredMethod("after").invoke(target);
        }
        return result;
    }
}
