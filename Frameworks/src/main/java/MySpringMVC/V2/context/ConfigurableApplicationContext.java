package MySpringMVC.V2.context;

import MySpringMVC.V2.beans.ListableBeanFactory;
import MySpringMVC.V2.beans.support.DefaultListableBeanFactory;

public interface ConfigurableApplicationContext extends ListableBeanFactory {

    /**
     * Load or refresh the persistent representation of the configuration,
     * which might an XML file, properties file, or relational database schema.
     * <p>As this is a startup method, it should destroy already created singletons
     * if it fails, to avoid dangling resources. In other words, after invocation
     * of that method, either all or no singletons at all should be instantiated.
     *
     * @throws Exception             if the bean factory could not be initialized
     * @throws IllegalStateException if already initialized and multiple refresh
     *                               attempts are not supported
     */
    void refresh() throws Exception;

    /**
     * Return the internal bean factory of this application context.
     * Can be used to access specific functionality of the underlying factory.
     * <p>Note: Do not use this to post-process the bean factory; singletons
     * will already have been instantiated before. Use a BeanFactoryPostProcessor
     * to intercept the BeanFactory setup process before beans get touched.
     * <p>Generally, this internal factory will only be accessible while the context
     * is active, that is, inbetween {@link #refresh()} and close.
     * The isActive flag can be used to check whether the context
     * is in an appropriate state.
     *
     * @return the underlying bean factory
     * @throws Exception if the context does not hold an internal
     *                   bean factory (usually if {@link #refresh()} hasn't been called yet or
     *                   if close has already been called)
     */
    DefaultListableBeanFactory getBeanFactory() throws Exception;
}
