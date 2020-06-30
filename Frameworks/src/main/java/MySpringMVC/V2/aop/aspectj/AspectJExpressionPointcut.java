package MySpringMVC.V2.aop.aspectj;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
    private Map<Method, Set<AbstractAspectJAdvice>> shadowMatchCache = new ConcurrentHashMap<>(32);

    public AspectJExpressionPointcut(String id, String expression) {
        this.id = id;
        //去除execution()
        expression = expression.replaceAll(EXECUTION + "\\(", "");
        expression = expression.substring(0, expression.length() - 1);
        //转换为标准正则
        expression = expression.replaceAll("\\*", ".*");
        this.expression = expression;
    }

    public Set<AbstractAspectJAdvice> getAdvices(Method method) {
        for (Map.Entry<Method, Set<AbstractAspectJAdvice>> entry : shadowMatchCache.entrySet()) {
            Method cached = entry.getKey();
            Class<?> impl = cached.getDeclaringClass();
            List<Class<?>> interfaces = Arrays.asList(impl.getInterfaces());
            Class<?> theInterface = method.getDeclaringClass();
            if (impl.equals(theInterface) || interfaces.contains(theInterface)) {
                if (cached.getName().equals(method.getName()) &&
                        cached.getReturnType().equals(method.getReturnType()) &&
                        equalParamTypes(cached.getParameterTypes(), method.getParameterTypes())) {
                    return entry.getValue();
                }
            }
        }
        return new LinkedHashSet<>();
    }

    private boolean equalParamTypes(Class<?>[] params1, Class<?>[] params2) {
        /* Avoid unnecessary cloning */
        if (params1.length == params2.length) {
            for (int i = 0; i < params1.length; i++) {
                if (params1[i] != params2[i]) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    public Set<AbstractAspectJAdvice> addAdvice(Method method, AbstractAspectJAdvice advice) {
        Set<AbstractAspectJAdvice> result = shadowMatchCache.computeIfAbsent(method, k -> new LinkedHashSet<>());
        result.add(advice);
        return result;
    }

    public boolean containsAdvice() {
        return !shadowMatchCache.isEmpty();
    }
}
