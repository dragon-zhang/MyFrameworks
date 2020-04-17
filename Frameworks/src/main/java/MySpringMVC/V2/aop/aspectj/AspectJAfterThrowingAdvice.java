package MySpringMVC.V2.aop.aspectj;

import MySpringMVC.V2.aop.intercept.MethodInterceptor;
import MySpringMVC.V2.aop.intercept.MethodInvocation;
import lombok.NoArgsConstructor;

/**
 * @author SuccessZhang
 */
@NoArgsConstructor
public class AspectJAfterThrowingAdvice extends AbstractAspectJAdvice implements MethodInterceptor {

    public AspectJAfterThrowingAdvice(Class<?> declaringClass, String methodName, AspectJExpressionPointcut pointcut, int declarationOrder) {
        super(declaringClass, methodName, pointcut, declarationOrder, AdviceType.afterThrowing);
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        try {
            return invocation.proceed();
        } catch (Throwable t) {
            super.invokeAdviceMethod(invocation, null, t.getCause());
            throw t;
        }
    }
}
