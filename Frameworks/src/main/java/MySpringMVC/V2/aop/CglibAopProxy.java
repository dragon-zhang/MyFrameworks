package MySpringMVC.V2.aop;

import MySpringMVC.V2.aop.framework.ReflectiveMethodInvocation;
import MySpringMVC.V2.aop.proxy.cglib.Enhancer;
import MySpringMVC.V2.aop.proxy.cglib.MethodInterceptor;
import MySpringMVC.V2.aop.proxy.cglib.MethodProxy;
import lombok.RequiredArgsConstructor;

import java.lang.reflect.Method;
import java.util.List;

/**
 * @author SuccessZhang
 */
@RequiredArgsConstructor
public final class CglibAopProxy implements AopProxy, MethodInterceptor {

    private final AdvisedSupport advised;

    @Override
    public Object getProxy() {
        return this.getProxy(null);
    }

    @Override
    public Object getProxy(ClassLoader classLoader) {
        return Enhancer.create(this.advised.getTargetClass(), this);
    }

    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        Class<?> targetClass = this.advised.getTargetClass();
        List<Object> chain = this.advised.getInterceptorsAndDynamicInterceptionAdvice(method, targetClass);
        ProxyMethodInvocation methodInvocation = new ReflectiveMethodInvocation(obj, this.advised.getTarget(), method, args, targetClass, chain);
        return methodInvocation.proceed();
    }
}
