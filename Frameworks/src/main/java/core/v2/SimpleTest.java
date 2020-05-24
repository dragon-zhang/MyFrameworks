package core.v2;

import MySpringMVC.V2.context.ApplicationEvent;
import MySpringMVC.V2.context.ApplicationListener;
import MySpringMVC.V2.context.ClassPathXmlApplicationContext;
import core.v2.controller.TestController;
import core.v2.pojo.Type;
import core.v2.service.TestService;

import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;

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

        context.addApplicationListener(new ApplicationListener<ApplicationEvent>() {
            @Override
            public void onApplicationEvent(ApplicationEvent event) {
                DateTimeFormatter sdf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                System.out.println(sdf.format(new Timestamp(event.getTimestamp()).toLocalDateTime()) + " " + event.getSource());
            }
        });
        context.publishEvent(new ApplicationEvent("MyEvent") {
            private static final long serialVersionUID = -8155246575559994676L;

            @Override
            public Object getSource() {
                return super.getSource();
            }
        });
        context.shutdownMulticaster();
    }
}
