package MySpringMVC.V2.beans;

public interface BeanFactory {

    /**
     * 根据beanName从IOC中获取bean
     */
    Object getBean(String beanName) throws Exception;

    /**
     * 根据beanName和args从IOC中获取bean
     */
    Object getBean(String beanName, Object... args) throws Exception;

    /**
     * 根据beanType从IOC中获取bean
     */
    <T> T getBean(Class<T> requiredType) throws Exception;

    /**
     * 根据beanType和args从IOC中获取bean
     */
    <T> T getBean(Class<T> requiredType, Object... args) throws Exception;

    /**
     * 根据beanName和beanType从IOC中获取bean
     */
    <T> T getBean(String beanName, Class<T> requiredType) throws Exception;

    /**
     * 根据beanName、beanType和args从IOC中获取bean
     */
    <T> T getBean(String beanName, Class<T> requiredType, Object... args) throws Exception;
}
