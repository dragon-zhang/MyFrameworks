package MySpringMVC.V2.beans.support;

import MySpringMVC.V2.annotation.Autowired;
import MySpringMVC.V2.beans.BeanWrapper;
import MySpringMVC.V2.beans.FactoryBean;
import MySpringMVC.V2.beans.InitializingBean;
import MySpringMVC.V2.beans.config.BeanDefinition;
import MySpringMVC.V2.beans.config.BeanPostProcessor;
import MySpringMVC.V2.beans.config.ConfigurableBeanFactory;
import MySpringMVC.V2.core.StringUtils;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public abstract class AbstractAutowireCapableBeanFactory extends DefaultSingletonBeanRegistry implements ConfigurableBeanFactory {

    /**
     * BeanPostProcessors to apply in createBean
     */
    private final Set<BeanPostProcessor> beanPostProcessors = new HashSet<>();

    /**
     * Cache of unfinished FactoryBean instances: FactoryBean name --> BeanWrapper
     */
    private final Map<String, BeanWrapper> factoryBeanInstanceCache = new ConcurrentHashMap<>(16);

    @Override
    public Object getBean(String beanName) throws Exception {
        return this.getBean(beanName, (Object[]) null);
    }

    @Override
    public Object getBean(String beanName, Object... args) throws Exception {
        return this.getBean(beanName, null, args);
    }

    @Override
    public <T> T getBean(Class<T> requiredType) throws Exception {
        return this.getBean(requiredType, (Object[]) null);
    }

    @Override
    public <T> T getBean(Class<T> requiredType, Object... args) throws Exception {
        return this.getBean(StringUtils.lowerFirstCase(requiredType.getSimpleName()), requiredType, args);
    }

    @Override
    public <T> T getBean(String beanName, Class<T> requiredType) throws Exception {
        return this.getBean(beanName, requiredType, (Object[]) null);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getBean(String beanName, Class<T> requiredType, Object... args) throws Exception {
        BeanDefinition beanDefinition = this.getBeanDefinition(beanName);
        if (beanDefinition == null) {
            return null;
        }
        Object bean;
        BeanWrapper beanWrapper = factoryBeanInstanceCache.get(beanName);
        if (beanWrapper != null) {
            bean = beanWrapper.getWrappedObject();
            if (requiredType == null || bean.getClass() == requiredType) {
                return (T) bean;
            }
            return null;
        }
        beanWrapper = createBeanInstance(beanName, beanDefinition, args);
        factoryBeanInstanceCache.put(beanName, beanWrapper);
        if (beanDefinition.isAutowire()) {
            populateBean(beanName, beanDefinition, beanWrapper);
        }
        bean = beanWrapper.getWrappedObject();
        for (BeanPostProcessor processor : beanPostProcessors) {
            processor.postProcessBeforeInitialization(bean, beanName);
        }
        bean = invokeInitMethods(beanName, bean, beanDefinition);
        for (BeanPostProcessor processor : beanPostProcessors) {
            processor.postProcessAfterInitialization(bean, beanName);
        }
        if (requiredType == null || bean.getClass() == requiredType) {
            return (T) bean;
        }
        return null;
    }

    @Override
    public void addBeanPostProcessor(BeanPostProcessor beanPostProcessor) {
        this.beanPostProcessors.add(beanPostProcessor);
    }

    private Object invokeInitMethods(String beanName, Object bean, BeanDefinition beanDefinition) throws Exception {
        if (bean instanceof InitializingBean) {
            ((InitializingBean) bean).afterPropertiesSet();
        }
        String initMethodName = beanDefinition.getInitMethodName();
        if (!beanDefinition.isAutowire() && !"".equals(initMethodName)) {
            //不自动装配，调用指定的init方法
            Method method = bean.getClass().getDeclaredMethod(initMethodName);
            bean = method.invoke(bean);
            log.info("bean with name:{} has init by invoking method:{}", beanName, method);
        }
        return bean;
    }

    private BeanWrapper createBeanInstance(String beanName, BeanDefinition beanDefinition, Object... args) throws Exception {
        Object instance;
        Class<?> beanClass = beanDefinition.getBeanClass();
        Class<?>[] argTypes = null;
        if (args != null) {
            argTypes = new Class[args.length];
            for (int i = 0; i < args.length; i++) {
                argTypes[i] = args[i].getClass();
            }
        }
        Constructor<?> constructor = beanClass.getConstructor(argTypes);
        if (beanDefinition.isSingleton()) {
            if (super.containsSingleton(beanName)) {
                instance = super.getSingleton(beanName);
            } else {
                instance = constructor.newInstance(args);
                super.registerSingleton(beanName, instance);
            }
        } else {
            instance = constructor.newInstance(args);
        }
        BeanWrapper beanWrapper = new BeanWrapper(new FactoryBean<>(instance, beanClass, beanDefinition.isSingleton()));
        beanWrapper.setWrappedObject(instance);
        beanWrapper.setWrappedClass(beanClass);
        return beanWrapper;
    }

    private void populateBean(String beanName, BeanDefinition beanDefinition, BeanWrapper beanWrapper) throws Exception {
        Object instance = beanWrapper.getWrappedObject();
        Class<?> beanClass = beanDefinition.getBeanClass();
        Field[] fields = beanClass.getDeclaredFields();
        for (Field field : fields) {
            Autowired autowired = field.getAnnotation(Autowired.class);
            if (autowired == null) {
                continue;
            }
            autowireByName(instance, field, autowired.value().trim());
            log.info("bean with name:{} has populated fields", beanName);
        }
    }

    private void autowireByName(Object instance, Field field, String autowiredBeanName) throws Exception {
        if ("".equals(autowiredBeanName)) {
            autowiredBeanName = StringUtils.lowerFirstCase(field.getType().getSimpleName());
        }
        field.setAccessible(true);
        field.set(instance, this.getBean(autowiredBeanName));
    }

    /**
     * Return the bean definition for the given bean name.
     * Subclasses should normally implement caching, as this method is invoked
     * by this class every time bean definition metadata is needed.
     * <p>Depending on the nature of the concrete bean factory implementation,
     * this operation might be expensive (for example, because of directory lookups
     * in external registries). However, for listable bean factories, this usually
     * just amounts to a local hash lookup: The operation is therefore part of the
     * public interface there. The same implementation can serve for both this
     * template method and the public interface method in that case.
     *
     * @param beanName the name of the bean to find a definition for
     * @return the BeanDefinition for this prototype name (never {@code null})
     * @throws Exception if the bean definition cannot be resolved in case of errors
     */
    protected abstract BeanDefinition getBeanDefinition(String beanName) throws Exception;

}
