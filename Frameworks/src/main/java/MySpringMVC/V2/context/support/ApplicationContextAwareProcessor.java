package MySpringMVC.V2.context.support;

import MySpringMVC.V2.beans.Aware;
import MySpringMVC.V2.beans.config.BeanPostProcessor;
import MySpringMVC.V2.context.ApplicationContextAware;

/**
 * @author SuccessZhang
 */
public class ApplicationContextAwareProcessor implements BeanPostProcessor {

    private final AbstractApplicationContext applicationContext;

    public ApplicationContextAwareProcessor(AbstractApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws Exception {
        invokeAwareInterfaces(bean);
        return bean;
    }

    private void invokeAwareInterfaces(Object bean) throws Exception {
        if (bean instanceof Aware) {
            if (bean instanceof ApplicationContextAware) {
                ((ApplicationContextAware) bean).setApplicationContext(this.applicationContext);
            }
        }
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws Exception {
        return bean;
    }
}
