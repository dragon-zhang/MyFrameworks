package MySpringMVC.V2.annotation;

import MySpringMVC.V2.core.annotation.AliasFor;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author SuccessZhang
 * @date 2020/03/28 17:00
 */
@SuppressWarnings("unused")
@Component
@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Service {

    @AliasFor(annotation = Component.class, attribute = "value")
    String value() default "";

}
