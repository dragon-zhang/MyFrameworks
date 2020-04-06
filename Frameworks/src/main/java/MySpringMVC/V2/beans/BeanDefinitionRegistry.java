package MySpringMVC.V2.beans;

import MySpringMVC.V2.beans.config.BeanDefinition;

public interface BeanDefinitionRegistry {

    /**
     * Register a new bean definition with this registry.
     * Must support RootBeanDefinition and ChildBeanDefinition.
     *
     * @param beanName       the name of the bean instance to register
     * @param beanDefinition definition of the bean instance to register
     * @throws Exception if the BeanDefinition is invalid
     */
    void registerBeanDefinition(String beanName, BeanDefinition beanDefinition) throws Exception;

    /**
     * Remove the BeanDefinition for the given name.
     *
     * @param beanName the name of the bean instance to register
     * @throws Exception if there is no such bean definition
     */
    void removeBeanDefinition(String beanName) throws Exception;

    /**
     * Return the BeanDefinition for the given bean name.
     *
     * @param beanName name of the bean to find a definition for
     * @return the BeanDefinition for the given name (never {@code null})
     * @throws Exception if there is no such bean definition
     */
    BeanDefinition getBeanDefinition(String beanName) throws Exception;

    /**
     * Check if this registry contains a bean definition with the given name.
     *
     * @param beanName the name of the bean to look for
     * @return if this registry contains a bean definition with the given name
     */
    boolean containsBeanDefinition(String beanName);

    /**
     * Return the names of all beans defined in this registry.
     *
     * @return the names of all beans defined in this registry,
     * or an empty array if none defined
     */
    String[] getBeanDefinitionNames();

    /**
     * Return the number of beans defined in the registry.
     *
     * @return the number of beans defined in the registry
     */
    int getBeanDefinitionCount();

}
