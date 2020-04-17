package MySpringMVC.V2.aop.aspectj;

import MySpringMVC.V2.aop.ProxyMethodInvocation;
import MySpringMVC.V2.aop.intercept.MethodInterceptor;
import MySpringMVC.V2.aop.intercept.MethodInvocation;
import lombok.NoArgsConstructor;

/**
 * @author SuccessZhang
 */
@NoArgsConstructor
public class AspectJAroundAdvice extends AbstractAspectJAdvice implements MethodInterceptor {

    public AspectJAroundAdvice(Class<?> declaringClass, String methodName, AspectJExpressionPointcut pointcut, int declarationOrder) {
        super(declaringClass, methodName, pointcut, declarationOrder, AdviceType.around);
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        MethodInvocationProceedingJoinPoint proceedingJoinPoint = new MethodInvocationProceedingJoinPoint((ProxyMethodInvocation) invocation);
        return super.invokeAdviceMethod(proceedingJoinPoint, null, null);
    }
}
