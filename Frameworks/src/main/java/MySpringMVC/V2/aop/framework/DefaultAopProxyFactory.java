package MySpringMVC.V2.aop.framework;

import MySpringMVC.V2.aop.AdvisedSupport;
import MySpringMVC.V2.aop.AopProxy;
import MySpringMVC.V2.aop.CglibAopProxy;
import MySpringMVC.V2.aop.JdkDynamicAopProxy;

/**
 * @author SuccessZhang
 * @date 2020/04/12
 */
public class DefaultAopProxyFactory {

    protected AopProxy createAopProxy(AdvisedSupport config) {
        if (config.getInterfaces().isEmpty()) {
            return new CglibAopProxy(config);
        }
        return new JdkDynamicAopProxy(config);
    }

}
