package MySpringMVC.V2.aop.aspectj;

import MySpringMVC.V2.aop.intercept.Advice;
import MySpringMVC.V2.aop.intercept.JoinPoint;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * before、after、around、afterReturning、afterThrowing通知的共同父类，
 * 对应xml中<aop:before/>等标签的内容。
 *
 * @author SuccessZhang
 * @date 2020/04/12
 */
@Data
@NoArgsConstructor
public abstract class AbstractAspectJAdvice implements Advice {

    protected Class<?> declaringClass;

    protected String methodName;

    protected Method aspectJAdviceMethod;

    protected AspectJExpressionPointcut pointcut;

    /**
     * The name of the aspect (ref bean) in which this advice was defined
     * (used when determining advice precedence so that we can determine
     * whether two pieces of advice come from the same aspect).
     */
    protected String aspectName;

    /**
     * The aspect (ref bean) in which this advice was defined
     * (used when determining advice precedence so that we can determine
     * whether two pieces of advice come from the same aspect).
     */
    protected Object aspect;

    /**
     * The order of declaration of this advice within the aspect.
     */
    protected int declarationOrder;

    /**
     * Non-null if after throwing advice binds the thrown value
     */
    protected String throwingName;

    protected Class<?> discoveredThrowingType = Object.class;

    /**
     * Non-null if after returning advice binds the return value
     */
    protected String returningName;

    protected Class<?> discoveredReturningType = Object.class;

    protected AdviceType adviceType;

    public AbstractAspectJAdvice(Class<?> declaringClass, String methodName, AspectJExpressionPointcut pointcut, int declarationOrder, AdviceType adviceType) {
        this.declaringClass = declaringClass;
        this.methodName = methodName;
        this.pointcut = pointcut;
        this.declarationOrder = declarationOrder;
        this.adviceType = adviceType;
    }

    /**
     * Invoke the advice method.
     *
     * @param joinPoint   the JoinPointMatch that matched this execution join point
     * @param returnValue the return value from the method execution (may be null)
     * @param ex          the exception thrown by the method execution (may be null)
     * @return the invocation result
     * @throws Throwable in case of invocation failure
     */
    protected Object invokeAdviceMethod(JoinPoint joinPoint, Object returnValue, Throwable ex) throws Throwable {
        Class<?>[] parameterTypes = this.aspectJAdviceMethod.getParameterTypes();
        if (parameterTypes.length == 0) {
            return this.aspectJAdviceMethod.invoke(this.aspect);
        }
        Object[] params = new Object[parameterTypes.length];
        for (int i = 0; i < params.length; i++) {
            if (parameterTypes[i] == JoinPoint.class) {
                params[i] = joinPoint;
            } else if (parameterTypes[i] == Throwable.class) {
                params[i] = ex;
            } else {
                params[i] = returnValue;
            }
        }
        return this.aspectJAdviceMethod.invoke(this.aspect, params);
    }

    public enum AdviceType {

        /**
         * 前置通知
         */
        before,

        /**
         * 后置通知
         */
        after,

        /**
         * 环绕通知
         */
        around,

        /**
         * 异常通知
         */
        afterThrowing,

        /**
         * 返回通知
         */
        afterReturning;

        private static final Map<String, AdviceType> MAP = new HashMap<>(AdviceType.values().length);

        static {
            for (AdviceType value : AdviceType.values()) {
                MAP.put(value.name(), value);
            }
        }

        public static AdviceType getAdviceType(String name) {
            return MAP.get(name);
        }
    }
}
