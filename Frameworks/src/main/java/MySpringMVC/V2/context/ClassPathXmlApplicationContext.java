package MySpringMVC.V2.context;

import MySpringMVC.V2.context.support.AbstractApplicationContext;

public class ClassPathXmlApplicationContext extends AbstractApplicationContext {

    public ClassPathXmlApplicationContext(String configLocation) {
        this(new String[]{configLocation});
    }

    public ClassPathXmlApplicationContext(String... configLocations) {
        super(configLocations);
    }

}
