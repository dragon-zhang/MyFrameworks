package MySpringBoot.initializer;

import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.WebResourceRoot;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.webresources.DirResourceSet;
import org.apache.catalina.webresources.StandardRoot;

import java.io.File;

/**
 * @author SuccessZhang
 */
public class MySpringBootApplication {

    /**
     * 启动Tomcat
     */
    public static void run(Class<?> clazz, String[] args) {
        Tomcat tomcat = new Tomcat();
        tomcat.setPort(9095);
        //设置需要发布的项目
        String sourcePath = clazz.getResource("/").getPath();
        //告知webapp的位置
        Context context = tomcat.addWebapp("", new File("src/main/webapp").getAbsolutePath());
        //告知class文件的位置
        WebResourceRoot resourceRoot = new StandardRoot(context);
        resourceRoot.addPreResources(new DirResourceSet(resourceRoot, "/WEB-INF/classes", sourcePath, "/"));
        context.setResources(resourceRoot);
        try {
            tomcat.start();
        } catch (LifecycleException e) {
            e.printStackTrace();
        }
        tomcat.getServer().await();
    }
}
