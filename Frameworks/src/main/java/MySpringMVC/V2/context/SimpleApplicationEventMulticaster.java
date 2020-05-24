package MySpringMVC.V2.context;

import MySpringMVC.V2.beans.BeanFactory;
import MySpringMVC.V2.beans.BeanFactoryAware;
import MySpringMVC.V2.beans.support.DefaultListableBeanFactory;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author SuccessZhang
 */
public class SimpleApplicationEventMulticaster implements ApplicationEventMulticaster, BeanFactoryAware {

    private final ExecutorService executor = Executors.newFixedThreadPool(6);

    private BeanFactory beanFactory;

    private final Set<ApplicationListener<? extends ApplicationEvent>> applicationListeners = new LinkedHashSet<>();

    public SimpleApplicationEventMulticaster(DefaultListableBeanFactory beanFactory) {
        this.setBeanFactory(beanFactory);
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    @Override
    public void addApplicationListener(ApplicationListener<? extends ApplicationEvent> listener) {
        this.applicationListeners.add(listener);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void addApplicationListenerBean(String listenerBeanName) {
        try {
            ApplicationListener<? extends ApplicationEvent> listener = beanFactory.getBean(listenerBeanName, ApplicationListener.class);
            if (listener != null) {
                this.addApplicationListener(listener);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void removeApplicationListener(ApplicationListener<? extends ApplicationEvent> listener) {
        this.applicationListeners.remove(listener);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void removeApplicationListenerBean(String listenerBeanName) {
        try {
            ApplicationListener<? extends ApplicationEvent> listener = beanFactory.getBean(listenerBeanName, ApplicationListener.class);
            if (listener != null) {
                this.removeApplicationListener(listener);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void removeAllListeners() {
        this.applicationListeners.clear();
    }

    @Override
    @SuppressWarnings("unchecked")
    public void multicastEvent(ApplicationEvent event) {
        for (ApplicationListener listener : this.applicationListeners) {
            this.executor.execute(() -> listener.onApplicationEvent(event));
        }
    }

    public void shutdown() {
        this.executor.shutdown();
    }

}
