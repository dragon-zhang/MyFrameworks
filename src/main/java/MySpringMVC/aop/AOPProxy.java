package MySpringMVC.aop;

import MySpringMVC.annotation.*;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.*;

/**
 * @author SuccessZhang
 */
public enum AOPProxy implements MethodInterceptor, InvocationHandler {

    /*枚举单例*/
    INSTANCE;

    private static Object target;

    /**
     * 获得增强之后的目标类，即添加了切入逻辑advice之后的目标类
     */
    public static <T> T getInstance(Class<T> type) {
        try {
            target = type.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        if (AOPMethods.class.isAssignableFrom(type)) {
            //如果该类实现了AOPMethods接口，采用性能更好的jdk动态代理
            return jdk8(type);
        } else {
            //如果该类未实现AOPMethods接口，采用cglib的动态代理
            return cglib(type);
        }
    }

    @SuppressWarnings("unchecked")
    private static <T> T cglib(Class<T> type) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(type);
        //回调方法的参数为代理类对象CglibProxy，最后增强目标类调用的是代理类对象CglibProxy中的intercept方法
        enhancer.setCallback(INSTANCE);
        // 此刻，base不是单纯的目标类，而是增强过的目标类
        return (T) enhancer.create();
    }

    @SuppressWarnings("unchecked")
    private static <T> T jdk8(Class<T> type) {
        if (!AOPMethods.class.isAssignableFrom(type)) {
            throw new RuntimeException("the " + type + " should implements " + AOPMethods.class);
        }
        return (T) Proxy.newProxyInstance(type.getClassLoader(),
                type.getInterfaces(), INSTANCE);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object result = null;
        Class<?> clazz = target.getClass();
        clazz.getDeclaredMethod("before").invoke(target);
        try {
            clazz.getDeclaredMethod("aroundBefore").invoke(target);
            result = method.invoke(target, args);
            clazz.getDeclaredMethod("afterReturning", Object.class).invoke(target, result);
        } catch (Throwable throwable) {
            clazz.getDeclaredMethod("afterThrowing", Throwable.class).invoke(target, throwable);
            throwable.printStackTrace();
        } finally {
            clazz.getDeclaredMethod("after").invoke(target);
        }
        clazz.getDeclaredMethod("aroundAfter").invoke(target);
        return result;
    }

    @Override
    public Object intercept(Object object, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        Object result = null;
        Class<?> cc = method.getDeclaringClass();
        Method[] methods = cc.getDeclaredMethods();
        before(methods);
        try {
            aroundBefore(methods);
            result = proxy.invokeSuper(object, args);
            afterReturn(methods, result);
        } catch (Throwable throwable) {
            afterThrow(methods, throwable);
            throwable.printStackTrace();
        } finally {
            after(methods);
        }
        aroundAfter(methods);
        return result;
    }

    private void common(Method method, String[] params, Object addition) throws InvocationTargetException, IllegalAccessException {
        Object[] legalParams;
        int length = method.getParameters().length;
        if (addition == null) {
            legalParams = new Object[length];
        } else {
            if (length == 0) {
                throw new RuntimeException(method + " requires at least one parameter !");
            }
            if (!(addition instanceof Throwable)) {
                Parameter[] parameters = method.getParameters();
                boolean flag = true;
                for (int i = 0; i < parameters.length && flag; i++) {
                    if ("result".equals(parameters[i].getName()) && parameters[i].getType().getName().endsWith("Object")) {
                        flag = false;
                    }
                }
                if (flag) {
                    throw new RuntimeException(method + " requires one and only one parameter which named 'result' and its parameterType must be 'java.lang.Object'!");
                }
            }
            legalParams = new Object[length];
            legalParams[length - 1] = addition;
        }
        if (params.length >= length && length != 1) {
            System.arraycopy(params, 0, legalParams, 0, length);
        } else if (params.length < length) {
            System.arraycopy(params, 0, legalParams, 0, params.length);
        }
        method.invoke(target, legalParams);
    }

    private void before(Method[] methods) throws IllegalAccessException, InvocationTargetException {
        for (Method method : methods) {
            if (method.isAnnotationPresent(Before.class)) {
                Before before = method.getAnnotation(Before.class);
                common(method, before.value(), null);
            }
        }
    }

    private void aroundBefore(Method[] methods) throws IllegalAccessException, InvocationTargetException {
        for (Method method : methods) {
            if (method.isAnnotationPresent(AroundBefore.class)) {
                AroundBefore aroundBefore = method.getAnnotation(AroundBefore.class);
                common(method, aroundBefore.value(), null);
            }
        }
    }

    private void afterReturn(Method[] methods, Object result) throws IllegalAccessException, InvocationTargetException {
        for (Method method : methods) {
            if (method.isAnnotationPresent(AfterReturning.class)) {
                AfterReturning afterReturning = method.getAnnotation(AfterReturning.class);
                common(method, afterReturning.value(), result);
            }
        }
    }

    private void afterThrow(Method[] methods, Throwable throwable) throws IllegalAccessException, InvocationTargetException {
        for (Method method : methods) {
            if (method.isAnnotationPresent(AfterThrowing.class)) {
                AfterThrowing afterThrowing = method.getAnnotation(AfterThrowing.class);
                common(method, afterThrowing.value(), throwable);
            }
        }
    }

    private void after(Method[] methods) throws IllegalAccessException, InvocationTargetException {
        for (Method method : methods) {
            if (method.isAnnotationPresent(After.class)) {
                After after = method.getAnnotation(After.class);
                common(method, after.value(), null);
            }
        }
    }

    private void aroundAfter(Method[] methods) throws IllegalAccessException, InvocationTargetException {
        for (Method method : methods) {
            if (method.isAnnotationPresent(AroundAfter.class)) {
                AroundAfter aroundAfter = method.getAnnotation(AroundAfter.class);
                common(method, aroundAfter.value(), null);
            }
        }
    }
}
