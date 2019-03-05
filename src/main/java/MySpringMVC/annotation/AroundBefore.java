package MySpringMVC.annotation;

import java.lang.annotation.*;

@SuppressWarnings("unused")
@Documented
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface AroundBefore {
    String[] value() default "";
}
