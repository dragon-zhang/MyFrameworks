package MySpringMVC.V2.beans.config;

import lombok.Data;

/**
 * @author SuccessZhang
 * @date 2020/04/06
 * @see org.springframework.beans.factory.support.AbstractBeanDefinition
 * @see org.springframework.beans.factory.support.BeanDefinitionBuilder
 * @see org.springframework.beans.factory.config.BeanDefinitionVisitor
 */
@Data
public class BeanDefinition {

    private boolean lazyInit;

    private final String scope;

    private final String factoryBeanName;

    private final String beanClassName;

    private final Class<?> beanClass;

    private final boolean autowire;

    private final String initMethodName;

    private final String destroyMethodName;

    public BeanDefinition(String scope, String factoryBeanName, String beanClassName, Class<?> beanClass, boolean autowire, String initMethodName, String destroyMethodName) {
        this.lazyInit = false;
        this.scope = scope;
        this.factoryBeanName = factoryBeanName;
        this.beanClassName = beanClassName;
        this.beanClass = beanClass;
        this.autowire = autowire;
        this.initMethodName = initMethodName;
        this.destroyMethodName = destroyMethodName;
    }

    public boolean isSingleton() {
        return ConfigurableBeanFactory.SCOPE_SINGLETON.equals(scope);
    }
}
