package MySpringMVC.V2.context;

import MySpringMVC.V2.beans.Aware;
import MySpringMVC.V2.context.support.AbstractApplicationContext;

/**
 * @author SuccessZhang
 * @date 2020/03/28 19:00
 */
public interface ApplicationContextAware extends Aware {

    /**
     * Set the ApplicationContext that this object runs in.
     * Normally this call will be used to initialize the object.
     */
    void setApplicationContext(AbstractApplicationContext applicationContext) throws Exception;

}