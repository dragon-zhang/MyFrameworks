package MySpringMVC.V2.aop.aspectj;

import MySpringMVC.V2.aop.intercept.JoinPoint;
import MySpringMVC.V2.aop.intercept.MethodInterceptor;
import MySpringMVC.V2.aop.intercept.MethodInvocation;
import lombok.NoArgsConstructor;

import java.lang.reflect.Method;

/**
 * @author SuccessZhang
 */
@NoArgsConstructor
public class AspectJMethodBeforeAdvice extends AbstractAspectJAdvice implements MethodInterceptor {

    private JoinPoint joinPoint;

    public AspectJMethodBeforeAdvice(Class<?> declaringClass, String methodName, AspectJExpressionPointcut pointcut, int declarationOrder) {
        super(declaringClass, methodName, pointcut, declarationOrder, AdviceType.before);
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        this.joinPoint = invocation;
        before(invocation.getMethod(), invocation.getArguments(), invocation.getThis());
        return invocation.proceed();
    }

    private void before(Method method, Object[] arguments, Object target) throws Throwable {
        Object returnValue = method.invoke(target, arguments);
        super.invokeAdviceMethod(joinPoint, returnValue, null);
    }
}
