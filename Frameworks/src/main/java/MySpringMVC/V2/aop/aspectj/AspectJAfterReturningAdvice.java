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
public class AspectJAfterReturningAdvice extends AbstractAspectJAdvice implements MethodInterceptor {

    private JoinPoint joinPoint;

    public AspectJAfterReturningAdvice(Class<?> declaringClass, String methodName, AspectJExpressionPointcut pointcut, int declarationOrder) {
        super(declaringClass, methodName, pointcut, declarationOrder, AdviceType.afterReturning);
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        joinPoint = invocation;
        Object returnValue = invocation.proceed();
        afterReturning(returnValue, invocation.getMethod(), invocation.getArguments(), invocation.getThis());
        return returnValue;
    }

    private void afterReturning(Object returnValue, Method method, Object[] arguments, Object target) throws Throwable {
        super.invokeAdviceMethod(joinPoint, returnValue, null);
    }
}
