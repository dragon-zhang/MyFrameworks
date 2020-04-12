package MySpringMVC.V2.beans.config;

import MySpringMVC.V2.beans.ListableBeanFactory;

import java.util.Iterator;

public interface ConfigurableListableBeanFactory extends ListableBeanFactory, ConfigurableBeanFactory {
    /**
     * Return the registered BeanDefinition for the specified bean, allowing access
     * to its property values and constructor argument value (which can be
     * modified during bean factory post-processing).
     * <p>A returned BeanDefinition object should not be a copy but the original
     * definition object as registered in the factory. This means that it should
     * be castable to a more specific implementation type, if necessary.
     * <p><b>NOTE:</b> This method does <i>not</i> consider ancestor factories.
     * It is only meant for accessing local bean definitions of this factory.
     *
     * @param beanName the name of the bean
     * @return the registered BeanDefinition
     * @throws Exception if there is no bean with the given name
     *                                       defined in this factory
     */
    BeanDefinition getBeanDefinition(String beanName) throws Exception;

    /**
     * Return a unified view over all bean names managed by this factory.
     * <p>Includes bean definition names as well as names of manually registered
     * singleton instances, with bean definition names consistently coming first,
     * analogous to how type/annotation specific retrieval of bean names works.
     *
     * @return the composite iterator for the bean names view
     * @see #containsBeanDefinition
     * @see #registerSingleton
     * @see #getBeanNamesForType
     * @see #getBeanNamesForAnnotation
     * @since 4.1.2
     */
    Iterator<String> getBeanNamesIterator();
}
