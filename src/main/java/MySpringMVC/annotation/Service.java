package MySpringMVC.annotation;

import java.lang.annotation.*;

@SuppressWarnings("unused")
@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Service {
    String value() default "";
}
