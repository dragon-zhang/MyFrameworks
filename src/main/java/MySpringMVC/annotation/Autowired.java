package MySpringMVC.annotation;

import java.lang.annotation.*;

@SuppressWarnings("unused")
@Documented
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Autowired {
    String value() default "";
}
