package MySpringMVC.V2.aop;

import MySpringMVC.V2.aop.framework.ReflectiveMethodInvocation;
import MySpringMVC.V2.core.proxy.jdk.InvocationHandler;
import MySpringMVC.V2.core.proxy.jdk.Proxy;
import lombok.RequiredArgsConstructor;

import java.lang.reflect.Method;
import java.util.List;

/**
 * @author SuccessZhang
 * @date 2020/04/12
 */
@RequiredArgsConstructor
public final class JdkDynamicAopProxy implements AopProxy, InvocationHandler {

    private final AdvisedSupport advised;

    @Override
    public Object getProxy() {
        return this.getProxy(this.advised.getTargetClass().getClassLoader());
    }

    @Override
    public Object getProxy(ClassLoader classLoader) {
        return Proxy.newProxyInstance(classLoader, this.advised.getTargetClass().getInterfaces(), this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Class<?> targetClass = this.advised.getTargetClass();
        List<Object> chain = this.advised.getInterceptorsAndDynamicInterceptionAdvice(method, targetClass);
        ProxyMethodInvocation methodInvocation = new ReflectiveMethodInvocation(proxy, this.advised.getTarget(), method, args, targetClass, chain);
        return methodInvocation.proceed();
    }
}
