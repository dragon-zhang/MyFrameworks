package MySpringMVC.V2.aop.framework;

import MySpringMVC.V2.aop.ProxyMethodInvocation;
import MySpringMVC.V2.aop.intercept.MethodInterceptor;
import MySpringMVC.V2.aop.intercept.MethodInvocation;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author SuccessZhang
 */
public class ReflectiveMethodInvocation implements ProxyMethodInvocation, Cloneable {

    protected final Object proxy;

    protected final Object target;

    protected final Method method;

    protected Object[] arguments = new Object[0];

    private final Class<?> targetClass;

    /**
     * Lazily initialized map of user-specific attributes for this invocation.
     */
    private Map<String, Object> userAttributes = new HashMap<>(16);

    /**
     * List of MethodInterceptor and InterceptorAndDynamicMethodMatcher
     * that need dynamic checks.
     */
    protected final List<?> interceptorsAndDynamicMethodMatchers;

    /**
     * Index from 0 of the current interceptor we're invoking.
     * -1 until we invoke: then the current interceptor.
     */
    private int currentInterceptorIndex = -1;

    /**
     * Construct a new ReflectiveMethodInvocation with the given arguments.
     *
     * @param proxy                                the proxy object that the invocation was made on
     * @param target                               the target object to invoke
     * @param method                               the method to invoke
     * @param arguments                            the arguments to invoke the method with
     * @param targetClass                          the target class, for MethodMatcher invocations
     * @param interceptorsAndDynamicMethodMatchers interceptors that should be applied,
     *                                             along with any InterceptorAndDynamicMethodMatchers that need evaluation at runtime.
     *                                             MethodMatchers included in this struct must already have been found to have matched
     *                                             as far as was possibly statically. Passing an array might be about 10% faster,
     *                                             but would complicate the code. And it would work only for static pointcuts.
     */
    public ReflectiveMethodInvocation(
            Object proxy, Object target, Method method, Object[] arguments,
            Class<?> targetClass, List<Object> interceptorsAndDynamicMethodMatchers) {
        this.proxy = proxy;
        this.target = target;
        this.targetClass = targetClass;
        this.method = method;
        this.arguments = arguments;
        this.interceptorsAndDynamicMethodMatchers = interceptorsAndDynamicMethodMatchers;
    }

    @Override
    public Object getProxy() {
        return this.proxy;
    }

    @Override
    public MethodInvocation invocableClone() {
        Object[] cloneArguments = this.arguments;
        if (this.arguments.length > 0) {
            // Build an independent copy of the arguments array.
            cloneArguments = new Object[this.arguments.length];
            System.arraycopy(this.arguments, 0, cloneArguments, 0, this.arguments.length);
        }
        return invocableClone(cloneArguments);
    }

    @Override
    public MethodInvocation invocableClone(Object... arguments) {
        // Create the MethodInvocation clone.
        try {
            ReflectiveMethodInvocation clone = (ReflectiveMethodInvocation) clone();
            clone.arguments = arguments;
            return clone;
        } catch (CloneNotSupportedException ex) {
            throw new IllegalStateException(
                    "Should be able to clone object of type [" + getClass() + "]: " + ex);
        }
    }

    @Override
    public void setArguments(Object... arguments) {
        this.arguments = arguments;
    }

    @Override
    public void setUserAttribute(String key, Object value) {
        if (value != null) {
            this.userAttributes.put(key, value);
        } else {
            this.userAttributes.remove(key);
        }
    }

    @Override
    public Object getUserAttribute(String key) {
        return this.userAttributes.get(key);
    }

    @Override
    public Method getMethod() {
        return this.method;
    }

    @Override
    public Object[] getArguments() {
        return this.arguments;
    }

    @Override
    public Object proceed() throws Throwable {
        //如果Interceptor执行完了，则执行joinPoint
        if (this.currentInterceptorIndex == this.interceptorsAndDynamicMethodMatchers.size() - 1) {
            return this.method.invoke(this.target, this.arguments);
        }
        Object interceptorOrInterceptionAdvice = this.interceptorsAndDynamicMethodMatchers.get(++this.currentInterceptorIndex);
        //如果要动态匹配joinPoint
        if (interceptorOrInterceptionAdvice instanceof MethodInterceptor) {
            // Evaluate dynamic method matcher here: static part will already have
            // been evaluated and found to match.
            MethodInterceptor dm = (MethodInterceptor) interceptorOrInterceptionAdvice;
            //动态匹配：运行时参数是否满足匹配条件
            return dm.invoke(this);
        } else {
            // Dynamic matching failed.
            // Skip this interceptor and invoke the next in the chain.
            //动态匹配失败时,略过当前Intercetpor,调用下一个Interceptor
            return proceed();
        }
    }

    @Override
    public Object getThis() {
        return this.target;
    }

    @Override
    public Object getTarget() {
        return this.target;
    }

    @Override
    public Object[] getArgs() {
        return this.getArguments();
    }

    @Override
    public AccessibleObject getStaticPart() {
        return this.method;
    }
}
