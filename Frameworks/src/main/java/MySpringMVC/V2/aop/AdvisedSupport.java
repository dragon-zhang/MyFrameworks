package MySpringMVC.V2.aop;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author SuccessZhang
 * @date 2020/04/11
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdvisedSupport {

    private final DefaultAdvisorChainFactory advisorChainFactory = new DefaultAdvisorChainFactory();

    private Object target;

    private Class<?> targetClass;

    /**
     * Cache with Method as key and advisor chain List as value
     */
    private Map<Method, List<Object>> methodCache = new ConcurrentHashMap<>(32);

    /**
     * Interfaces to be implemented by the proxy. Held in List to keep the order
     * of registration, to create JDK proxy with specified order of interfaces.
     */
    private List<Class<?>> interfaces;

    /**
     * List of Advisors. If an Advice is added, it will be wrapped
     * in an Advisor before being added to this List.
     */
    private List<Object> advisors = new LinkedList<>();

    /**
     * Add all of the given advisors to this proxy configuration.
     *
     * @param advisors the advisors to register
     */
    public void addAdvisors(Object... advisors) {
        addAdvisors(Arrays.asList(advisors));
    }

    /**
     * Add all of the given advisors to this proxy configuration.
     *
     * @param advisors the advisors to register
     */
    public void addAdvisors(Collection<Object> advisors) {
        this.advisors.addAll(advisors);
    }

    /**
     * Determine a list of {@link org.aopalliance.intercept.MethodInterceptor} objects
     * for the given method, based on this configuration.
     *
     * @param method      the proxied method
     * @param targetClass the target class
     * @return List of MethodInterceptors (may also include InterceptorAndDynamicMethodMatchers)
     */
    public List<Object> getInterceptorsAndDynamicInterceptionAdvice(Method method, Class<?> targetClass) {
        List<Object> cached = this.methodCache.get(method);
        if (cached == null) {
            cached = this.advisorChainFactory.getInterceptorsAndDynamicInterceptionAdvice(this, method, targetClass);
            this.methodCache.put(method, cached);
        }
        return cached;
    }
}
