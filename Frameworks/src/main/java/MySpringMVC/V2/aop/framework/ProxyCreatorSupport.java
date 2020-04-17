package MySpringMVC.V2.aop.framework;

import MySpringMVC.V2.aop.AdvisedSupport;
import MySpringMVC.V2.aop.AopProxy;
import lombok.NoArgsConstructor;

/**
 * @author SuccessZhang
 */
@NoArgsConstructor
public class ProxyCreatorSupport extends AdvisedSupport {

    private final DefaultAopProxyFactory proxyFactory = new DefaultAopProxyFactory();

    protected synchronized AopProxy createAopProxy() {
        return proxyFactory.createAopProxy(this);
    }
}
