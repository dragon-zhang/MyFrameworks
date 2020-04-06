package MySpringMVC.V2.beans.config;

import MySpringMVC.V2.beans.BeanFactory;

public interface ConfigurableBeanFactory extends SingletonBeanRegistry, BeanFactory {

    /**
     * Scope identifier for the standard singleton scope: "singleton".
     */
    String SCOPE_SINGLETON = "singleton";

    /**
     * Scope identifier for the standard prototype scope: "prototype".
     */
    String SCOPE_PROTOTYPE = "prototype";

    /**
     * Add a new BeanPostProcessor that will get applied to beans created
     * by this factory. To be invoked during factory configuration.
     * <p>Note: Post-processors submitted here will be applied in the order of
     * registration; any ordering semantics expressed through implementing the
     * {@link org.springframework.core.Ordered} interface will be ignored. Note
     * that autodetected post-processors (e.g. as beans in an ApplicationContext)
     * will always be applied after programmatically registered ones.
     *
     * @param beanPostProcessor the post-processor to register
     */
    void addBeanPostProcessor(BeanPostProcessor beanPostProcessor);
}
