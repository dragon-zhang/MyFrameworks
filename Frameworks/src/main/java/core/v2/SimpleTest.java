package core.v2;

import MySpringMVC.V2.context.ClassPathXmlApplicationContext;
import core.v2.controller.TestController;
import core.v2.pojo.Type;
import core.v2.service.TestService;

/**
 * @author SuccessZhang
 */
public class SimpleTest {
    public static void main(String[] args) throws Exception {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
        System.out.println(context);
        TestController controller = (TestController) context.getBean("testController");
        System.out.println(controller);
        TestService testService = (TestService) context.getBean("testService");
        System.out.println(testService);
        System.out.println(testService.setType("1", new Type("vip")));
    }
}
