package MySpringMVC.V2.aop.proxy.cglib;

import java.lang.reflect.Method;

/**
 * @author SuccessZhang
 * @date 2020/04/11
 */
@SuppressWarnings("unused")
public class MethodProxy {

    private final FastClass fastClass;

    /**
     * 子类重写的同名方法
     */
    private final Method superMethod;

    public MethodProxy(FastClass fastClass, Method superMethod) {
        this.fastClass = fastClass;
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
        return this.fastClass.invoke(superMethod, args);
    }
}
