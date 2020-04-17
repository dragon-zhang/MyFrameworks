package MySpringMVC.V2.aop.framework;

/**
 * @author SuccessZhang
 */
public class ProxyFactory extends ProxyCreatorSupport {

    /**
     * Create a new proxy according to the settings in this factory.
     * <p>Can be called repeatedly. Effect will vary if we've added
     * or removed interfaces. Can add and remove interceptors.
     * <p>Uses a default class loader: Usually, the thread context class loader
     * (if necessary for proxy creation).
     *
     * @return the proxy object
     */
    public Object getProxy() {
        return createAopProxy().getProxy();
    }

    /**
     * Create a new proxy according to the settings in this factory.
     * <p>Can be called repeatedly. Effect will vary if we've added
     * or removed interfaces. Can add and remove interceptors.
     * <p>Uses the given class loader (if necessary for proxy creation).
     *
     * @param classLoader the class loader to create the proxy with
     *                    (or {@code null} for the low-level proxy facility's default)
     * @return the proxy object
     */
    public Object getProxy(ClassLoader classLoader) {
        return createAopProxy().getProxy(classLoader);
    }
}
