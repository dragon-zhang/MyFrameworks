package MySpringMVC.V2.aop;

import MySpringMVC.V2.aop.aspectj.AbstractAspectJAdvice;
import MySpringMVC.V2.aop.aspectj.AspectJExpressionPointcut;

import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

/**
 * @author SuccessZhang
 * @date 2020/04/12
 */
@SuppressWarnings("unused")
public class DefaultAdvisorChainFactory {
    public List<Object> getInterceptorsAndDynamicInterceptionAdvice(AdvisedSupport advisedSupport, Method method, Class<?> targetClass) {
        List<Object> advices = new LinkedList<>();
        for (Object advisor : advisedSupport.getAdvisors()) {
            AspectJExpressionPointcut pointcut = (AspectJExpressionPointcut) advisor;
            List<AbstractAspectJAdvice> adviceList = pointcut.getAdvices(method);
            if (!adviceList.isEmpty()) {
                advices.addAll(adviceList);
            }
        }
        return advices;
    }
}
