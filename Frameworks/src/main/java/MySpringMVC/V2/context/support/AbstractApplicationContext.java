package MySpringMVC.V2.context.support;

import MySpringMVC.V2.aop.aspectj.AspectJAwareAdvisorAutoProxyCreator;
import MySpringMVC.V2.beans.config.BeanDefinition;
import MySpringMVC.V2.beans.support.BeanDefinitionReader;
import MySpringMVC.V2.beans.support.DefaultListableBeanFactory;
import MySpringMVC.V2.context.ApplicationEvent;
import MySpringMVC.V2.context.ApplicationEventMulticaster;
import MySpringMVC.V2.context.ApplicationEventPublisher;
import MySpringMVC.V2.context.ApplicationListener;
import MySpringMVC.V2.context.ConfigurableApplicationContext;
import MySpringMVC.V2.context.SimpleApplicationEventMulticaster;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * @author SuccessZhang
 */
public abstract class AbstractApplicationContext implements ConfigurableApplicationContext, ApplicationEventPublisher {

    private BeanDefinitionReader reader;

    private String[] configLocations;

    private DefaultListableBeanFactory beanFactory;

    private ApplicationEventMulticaster applicationEventMulticaster;

    public AbstractApplicationContext(String... configLocations) {
        this.configLocations = configLocations;
        this.beanFactory = new DefaultListableBeanFactory();
        try {
            refresh();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void prepareBeanFactory(DefaultListableBeanFactory beanFactory) {
        beanFactory.addBeanPostProcessor(new ApplicationContextAwareProcessor(this));
        beanFactory.addBeanPostProcessor(new AspectJAwareAdvisorAutoProxyCreator(this));
    }

    @Override
    public void refresh() throws Exception {
        //1.定位配置文件
        reader = new BeanDefinitionReader(this.configLocations);
        //2.加载配置文件
        List<BeanDefinition> beanDefinitions = reader.loadBeanDefinitions();
        //3.将bean的配置信息注册到伪IOC容器
        // (就是上方的DefaultListableBeanFactory中的beanDefinitionMap)
        doRegisterBeanDefinitions(beanDefinitions);
        prepareBeanFactory(this.beanFactory);
        //初始化容器事件传播器.
        initApplicationEventMulticaster();
        //为事件传播器注册事件监听器.
        registerListeners();
        //4.把非延迟加载的类提前初始化
        finishBeanFactoryInitialization(beanDefinitions);
    }

    @SuppressWarnings("unchecked")
    private void registerListeners() throws Exception {
        Collection<ApplicationListener> collection = this.beanFactory.getBeansOfType(ApplicationListener.class).values();
        for (ApplicationListener listener : collection) {
            try {
                this.applicationEventMulticaster.addApplicationListener(listener);
            } catch (Exception ignored) {
            }
        }
    }

    private void initApplicationEventMulticaster() {
        this.applicationEventMulticaster = new SimpleApplicationEventMulticaster(this.beanFactory);
    }

    @Override
    public DefaultListableBeanFactory getBeanFactory() {
        return this.beanFactory;
    }

    private void doRegisterBeanDefinitions(List<BeanDefinition> beanDefinitions) {
        for (BeanDefinition beanDefinition : beanDefinitions) {
            this.beanFactory.registerBeanDefinition(beanDefinition.getFactoryBeanName(), beanDefinition);
        }
    }

    private void finishBeanFactoryInitialization(List<BeanDefinition> beanDefinitions) {
        for (BeanDefinition beanDefinition : beanDefinitions) {
            if (beanDefinition.isLazyInit()) {
                continue;
            }
            try {
                this.beanFactory.getBean(beanDefinition.getFactoryBeanName());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public Properties getConfig() {
        return this.reader.getConfig();
    }

    @Override
    public Object getBean(String beanName) throws Exception {
        return this.beanFactory.getBean(beanName);
    }

    @Override
    public Object getBean(String beanName, Object... args) throws Exception {
        return this.beanFactory.getBean(beanName, args);
    }

    @Override
    public <T> T getBean(Class<T> requiredType) throws Exception {
        return this.beanFactory.getBean(requiredType);
    }

    @Override
    public <T> T getBean(Class<T> requiredType, Object... args) throws Exception {
        return this.beanFactory.getBean(requiredType, args);
    }

    @Override
    public <T> T getBean(String beanName, Class<T> requiredType) throws Exception {
        return this.beanFactory.getBean(beanName, requiredType);
    }

    @Override
    public <T> T getBean(String beanName, Class<T> requiredType, Object... args) throws Exception {
        return this.beanFactory.getBean(beanName, requiredType, args);
    }

    @Override
    public <T> Map<String, T> getBeansOfType(Class<T> type) throws Exception {
        return this.beanFactory.getBeansOfType(type);
    }

    @Override
    public String[] getBeanNamesForAnnotation(Class<? extends Annotation> annotationType) {
        return this.beanFactory.getBeanNamesForAnnotation(annotationType);
    }

    @Override
    public Map<String, Object> getBeansWithAnnotation(Class<? extends Annotation> annotationType) throws Exception {
        return this.beanFactory.getBeansWithAnnotation(annotationType);
    }

    @Override
    public void publishEvent(ApplicationEvent event) {
        if (event != null) {
            this.applicationEventMulticaster.multicastEvent(event);
        }
    }

    public void addApplicationListener(ApplicationListener<? extends ApplicationEvent> listener) {
        this.applicationEventMulticaster.addApplicationListener(listener);
    }

    public void shutdownMulticaster() {
        ((SimpleApplicationEventMulticaster) this.applicationEventMulticaster).shutdown();
    }
}
