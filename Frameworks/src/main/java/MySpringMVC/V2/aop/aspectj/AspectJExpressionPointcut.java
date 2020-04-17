package MySpringMVC.V2.aop.aspectj;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 对应xml中<aop:pointcut/>标签的内容。
 *
 * @author SuccessZhang
 * @date 2020/04/12
 */
@NoArgsConstructor
public class AspectJExpressionPointcut {

    private static final String EXECUTION = "execution";

    @Getter
    @Setter
    private String id;

    @Getter
    @Setter
    private String expression;

    /**
     * 原生方法对应的拦截器链缓存
     */
    private Map<Method, List<AbstractAspectJAdvice>> shadowMatchCache = new ConcurrentHashMap<>(32);

    public AspectJExpressionPointcut(String id, String expression) {
        this.id = id;
        //去除execution()
        expression = expression.replaceAll(EXECUTION + "\\(", "");
        expression = expression.substring(0, expression.length() - 1);
        //转换为标准正则
        expression = expression.replaceAll("\\*", ".*");
        this.expression = expression;
    }

    public List<AbstractAspectJAdvice> getAdvices(Method method) {
        return shadowMatchCache.computeIfAbsent(method, k -> new LinkedList<>());
    }

    public List<AbstractAspectJAdvice> addAdvice(Method method, AbstractAspectJAdvice advice) {
        List<AbstractAspectJAdvice> result = shadowMatchCache.computeIfAbsent(method, k -> new LinkedList<>());
        result.add(advice);
        return result;
    }

    public boolean containsAdvice() {
        return !shadowMatchCache.isEmpty();
    }
}
