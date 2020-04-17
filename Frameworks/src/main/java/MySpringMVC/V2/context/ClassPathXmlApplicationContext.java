package MySpringMVC.V2.context;

import MySpringMVC.V2.context.support.AbstractApplicationContext;

/**
 * @author SuccessZhang
 * @date 2020/04/06
 */
public class ClassPathXmlApplicationContext extends AbstractApplicationContext {

    public ClassPathXmlApplicationContext(String configLocation) {
        this(new String[]{configLocation});
    }

    public ClassPathXmlApplicationContext(String... configLocations) {
        super(configLocations);
    }

}
