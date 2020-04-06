package MySpringMVC.V2.beans.support;

import MySpringMVC.V2.beans.BeanDefinitionRegistry;
import MySpringMVC.V2.beans.config.BeanDefinition;
import MySpringMVC.V2.beans.config.ConfigurableListableBeanFactory;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultListableBeanFactory extends AbstractAutowireCapableBeanFactory implements ConfigurableListableBeanFactory, BeanDefinitionRegistry {

    /**
     * Map of bean definition objects, keyed by bean name.
     */
    private final Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>(256);

    @Override
    public void registerBeanDefinition(String beanName, BeanDefinition beanDefinition) {
        beanDefinitionMap.put(beanName, beanDefinition);
    }

    @Override
    public void removeBeanDefinition(String beanName) {
        beanDefinitionMap.remove(beanName);
    }

    @Override
    public BeanDefinition getBeanDefinition(String beanName) {
        return beanDefinitionMap.get(beanName);
    }

    @Override
    public Iterator<String> getBeanNamesIterator() {
        return beanDefinitionMap.keySet().iterator();
    }

    @Override
    public boolean containsBeanDefinition(String beanName) {
        return beanDefinitionMap.containsKey(beanName);
    }

    @Override
    public String[] getBeanDefinitionNames() {
        return beanDefinitionMap.keySet().toArray(new String[0]);
    }

    @Override
    public int getBeanDefinitionCount() {
        return beanDefinitionMap.keySet().size();
    }

    @Override
    public <T> Map<String, T> getBeansOfType(Class<T> type) throws Exception {
        String[] beanNames = this.getBeanDefinitionNames();
        Map<String, T> result = new LinkedHashMap<>(beanNames.length);
        for (String beanName : beanNames) {
            T bean = super.getBean(beanName, type);
            if (bean == null) {
                continue;
            }
            result.put(beanName, bean);
        }
        return result;
    }

    @Override
    public String[] getBeanNamesForAnnotation(Class<? extends Annotation> annotationType) {
        List<String> result = new ArrayList<>();
        String[] beanNames = this.getBeanDefinitionNames();
        for (String beanName : beanNames) {
            try {
                BeanDefinition beanDefinition = this.getBeanDefinition(beanName);
                Class<?> beanClass = beanDefinition.getBeanClass();
                if (beanClass.isAnnotationPresent(annotationType)) {
                    result.add(beanName);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result.toArray(new String[0]);
    }

    @Override
    public Map<String, Object> getBeansWithAnnotation(Class<? extends Annotation> annotationType) throws Exception {
        String[] beanNames = this.getBeanNamesForAnnotation(annotationType);
        Map<String, Object> result = new LinkedHashMap<>(beanNames.length);
        for (String beanName : beanNames) {
            result.put(beanName, super.getBean(beanName));
        }
        return result;
    }
}
