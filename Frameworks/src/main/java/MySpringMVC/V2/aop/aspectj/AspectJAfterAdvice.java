package MySpringMVC.V2.aop.aspectj;

import MySpringMVC.V2.aop.intercept.MethodInterceptor;
import MySpringMVC.V2.aop.intercept.MethodInvocation;
import lombok.NoArgsConstructor;

/**
 * @author SuccessZhang
 */
@NoArgsConstructor
public class AspectJAfterAdvice extends AbstractAspectJAdvice implements MethodInterceptor {

    public AspectJAfterAdvice(Class<?> declaringClass, String methodName, AspectJExpressionPointcut pointcut, int declarationOrder) {
        super(declaringClass, methodName, pointcut, declarationOrder, AdviceType.after);
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        try {
            return invocation.proceed();
        } finally {
            super.invokeAdviceMethod(invocation, null, null);
        }
    }
}
