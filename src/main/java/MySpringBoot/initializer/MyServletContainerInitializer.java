package MySpringBoot.initializer;

import MySpringBoot.MyApplication;
import MySpringBoot.annotation.ScanPackage;
import MySpringBoot.servlet.BootDispatchServlet;
import MySpringBoot.servlet.StaticServlet;

import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import java.util.Set;

/**
 * @author SuccessZhang
 * 遵循servlet3.0动态热拔插规范
 */
public class MyServletContainerInitializer implements ServletContainerInitializer {

    @Override
    public void onStartup(Set<Class<?>> c, ServletContext ctx) throws ServletException {
        //1.创建并注册DispatcherServlet，完成applicationContext.xml配置的扫描
        if (MyApplication.class.isAnnotationPresent(ScanPackage.class)) {
            String scanPackage = MyApplication.class.getAnnotation(ScanPackage.class).value();
            if (!"".equals(scanPackage)) {
                ServletRegistration.Dynamic dispatcherServlet = ctx.addServlet("DispatcherServlet", new BootDispatchServlet(scanPackage));
                dispatcherServlet.setLoadOnStartup(1);
                dispatcherServlet.addMapping("/test/*");
            }
        }
        //2.创建并注册处理静态资源的servlet
        ServletRegistration.Dynamic staticServlet = ctx.addServlet("StaticServlet", new StaticServlet());
        staticServlet.setLoadOnStartup(1);
        staticServlet.addMapping("/static/*");
    }
}
