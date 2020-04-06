package MySpringMVC.V2.core;

import MySpringMVC.V2.annotation.Bean;
import MySpringMVC.V2.annotation.Component;
import MySpringMVC.V2.annotation.Controller;
import MySpringMVC.V2.annotation.Service;
import core.v2.controller.TestController;

import javax.servlet.annotation.WebServlet;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public class AnnotationUtils {

    public static Annotation getCoveredAnnotation(Class<?> beanClass) throws Exception {
        Annotation[] annotations = beanClass.getDeclaredAnnotations();
        for (Annotation annotation : annotations) {
            Class<?> annotationType = annotation.annotationType();
            if (!(annotation instanceof WebServlet ||
                    annotation instanceof Component ||
                    annotation instanceof Bean ||
                    annotation instanceof Controller ||
                    annotation instanceof Service)) {
                continue;
            }
            String methodName = "value";
            if (annotation instanceof WebServlet) {
                methodName = "name";
            }
            Method method = annotationType.getDeclaredMethod(methodName);
            Component component = annotationType.getAnnotation(Component.class);
            if (annotation instanceof WebServlet || component != null) {
                if (annotation instanceof Bean) {
                    return annotation;
                }
                return new Component() {
                    @Override
                    public Class<? extends Annotation> annotationType() {
                        return Component.class;
                    }

                    @Override
                    public String value() {
                        try {
                            return (String) method.invoke(annotation);
                        } catch (Exception e) {
                            return null;
                        }
                    }
                };
            }
        }
        return null;
    }

    public static void main(String[] args) throws Exception {
        System.out.println(((Component) getCoveredAnnotation(TestController.class)).value());
    }
}
