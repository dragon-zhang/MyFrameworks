package MySpringMVC.V2.aop.aspectj;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 对应xml中<aop:aspect/>标签中的内容
 *
 * @author SuccessZhang
 * @date 2020/04/12
 */
@Data
@NoArgsConstructor
public class AspectJPointcutAdvisor {

    private AbstractAspectJAdvice advice;

    private AspectJExpressionPointcut pointcut;

}
