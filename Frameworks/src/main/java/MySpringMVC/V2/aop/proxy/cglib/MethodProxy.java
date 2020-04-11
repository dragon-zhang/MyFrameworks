package MySpringMVC.V2.aop.proxy.cglib;

import MySpringMVC.V2.aop.proxy.ProxyHelper;

import java.lang.reflect.Method;

/**
 * @author SuccessZhang
 * @date 2020/04/11
 */
@SuppressWarnings("unused")
public class MethodProxy {

    /**
     * 子类重写的同名方法
     */
    private final Method superMethod;

    public MethodProxy(Method superMethod) {
        this.superMethod = superMethod;
    }

    /**
     * Invoke the original (super) method on the specified object.
     *
     * @param obj  the enhanced object, must be the object passed as the first
     *             argument to the MethodInterceptor
     * @param args the arguments passed to the intercepted method; you may substitute a different
     *             argument array as long as the types are compatible
     * @throws Throwable the bare exceptions thrown by the called method are passed through
     *                   without wrapping in an <code>InvocationTargetException</code>
     * @see MethodInterceptor#intercept
     */
    public Object invokeSuper(Object obj, Object... args) throws Throwable {
        Class<?> superClass = superMethod.getDeclaringClass();
        Object superInstance = ProxyHelper.getClassInstance(superClass);
        if (superInstance == null) {
            superInstance = superClass.newInstance();
            ProxyHelper.putClassInstance(superClass, superInstance);
        }
        return this.superMethod.invoke(superInstance, args);
    }
}
