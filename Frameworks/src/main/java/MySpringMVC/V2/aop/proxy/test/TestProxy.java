package MySpringMVC.V2.aop.proxy.test;

import MySpringMVC.V2.aop.proxy.cglib.Enhancer;
import MySpringMVC.V2.aop.proxy.cglib.MethodInterceptor;
import MySpringMVC.V2.aop.proxy.cglib.MethodProxy;
import MySpringMVC.V2.aop.proxy.jdk.InvocationHandler;
import MySpringMVC.V2.aop.proxy.jdk.Proxy;
import MySpringMVC.V2.core.ClassUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author SuccessZhang
 * @date 2020/04/11
 */
public enum TestProxy implements MethodInterceptor, InvocationHandler {

    //枚举单例
    INSTANCE;

    private static Object target;

    private static Class<?> type;

    private Map<Class<?>, List<Class<?>>> implCache = new HashMap<>(8);

    private Map<Class<?>, Object> classInstanceCache = new HashMap<>(16);

    @SuppressWarnings("unchecked")
    public static <T> T getInstance(Class<T> type) {
        if (type.isInterface()) {
            TestProxy.type = type;
            return (T) Proxy.newProxyInstance(type.getClassLoader(), new Class<?>[]{type}, INSTANCE);
        } else if (!Modifier.toString(type.getModifiers()).contains("final")) {
            try {
                target = type.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
            return (T) Enhancer.create(type, INSTANCE);
        }
        throw new RuntimeException("the modifiers should not contains 'final' !");
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object result = null;
        try {
            System.out.println("aroundBefore");
            System.out.println("before");
            List<Class<?>> classes = implCache.get(type);
            if (classes == null) {
                classes = ClassUtils.getAllImplementationByInterface(type);
                implCache.put(type, classes);
            }
            for (Class<?> clazz : classes) {
                Object instance = classInstanceCache.get(clazz);
                if (instance == null) {
                    instance = clazz.newInstance();
                    classInstanceCache.put(clazz, instance);
                }
                result = method.invoke(instance, args);
            }
            System.out.println("aroundAfter");
            System.out.println("afterReturning");
        } catch (Throwable throwable) {
            System.out.println("afterThrowing");
            throwable.printStackTrace();
        } finally {
            System.out.println("after");
        }
        return result;
    }

    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        Object result = null;
        Class<?> clazz = target.getClass();
        try {
            clazz.getDeclaredMethod("aroundBefore").invoke(target);
            clazz.getDeclaredMethod("before").invoke(target);
            result = proxy.invokeSuper(obj, args);
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
