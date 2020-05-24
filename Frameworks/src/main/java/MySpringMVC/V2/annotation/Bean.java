package MySpringMVC.V2.annotation;

import MySpringMVC.V2.core.annotation.AliasFor;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author SuccessZhang
 * @date 2020/04/02 23:22
 */
@Component
@Documented
@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Bean {

    /**
     * The name of this bean, or if several names, a primary bean name plus aliases.
     * <p>If left unspecified, the name of the bean is the name of the annotated method.
     * If specified, the method name is ignored.
     * <p>The bean name and aliases may also be configured via the attribute
     * if no other attributes are declared.
     */
    @AliasFor(annotation = Component.class, attribute = "value")
    String value() default "";

    /**
     * Are dependencies to be injected via convention-based autowiring by name or type?
     * <p>Note that this autowire mode is just about externally driven autowiring based
     * on bean property setter methods by convention, analogous to XML bean definitions.
     * <p>The default mode does allow for annotation-driven autowiring. "no" refers to
     * externally driven autowiring only, not affecting any autowiring demands that the
     * bean class itself expresses through annotations.
     */
    boolean autowire() default false;

    /**
     * The optional name of a method to call on the bean instance during initialization.
     * Not commonly used, given that the method may be called programmatically directly
     * within the body of a Bean-annotated method.
     * <p>The default value is {@code ""}, indicating no init method to be called.
     */
    String initMethod() default "";

    /**
     * The optional name of a method to call on the bean instance upon closing the
     * application context, for example a {@code close()} method on a JDBC
     * {@code DataSource} implementation, or a Hibernate {@code SessionFactory} object.
     * The method must have no arguments but may throw any exception.
     * <p>As a convenience to the user, the container will attempt to infer a destroy
     * method against an object returned from the {@code @Bean} method. For example, given
     * an {@code @Bean} method returning an Apache Commons DBCP {@code BasicDataSource},
     * the container will notice the {@code close()} method available on that object and
     * automatically register it as the {@code destroyMethod}. This 'destroy method
     * inference' is currently limited to detecting only public, no-arg methods named
     * 'close' or 'shutdown'. The method may be declared at any level of the inheritance
     * hierarchy and will be detected regardless of the return type of the {@code @Bean}
     * method (i.e., detection occurs reflectively against the bean instance itself at
     * creation time).
     * <p>To disable destroy method inference for a particular {@code @Bean}, specify an
     * empty string as the value, e.g. {@code @Bean(destroyMethod="")}. Note that the
     * callback interface will nevertheless get detected and the corresponding destroy
     * method invoked: In other words, {@code destroyMethod=""} only affects custom
     * close/shutdown methods and @link java.io.Closeable}/{@link java.lang.AutoCloseable}
     * declared close methods.
     * <p>Note: Only invoked on beans whose lifecycle is under the full control of the
     * factory, which is always the case for singletons but not guaranteed for any
     * other scope.
     */
    String destroyMethod() default "";

}