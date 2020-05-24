package MySpringMVC.V2.core.annotation.test;

import MySpringMVC.V2.core.annotation.AliasFor;
import MySpringMVC.V2.core.annotation.AnnotationUtils;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2019-06-06 21:11
 */

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2019-06-06 21:11
 */
public class SynthesizedAnnotationTest {

    @Target({ANNOTATION_TYPE, FIELD, TYPE})
    @Retention(RUNTIME)
    @interface Test1 {
        String test1() default "test1";
    }

    @Target({ANNOTATION_TYPE, FIELD, TYPE})
    @Retention(RUNTIME)
    @Test1
    @interface Test2 {
        @AliasFor(annotation = Test1.class, attribute = "test1")
        String test2() default "test2";
    }

    @Target({ANNOTATION_TYPE, FIELD, TYPE})
    @Retention(RUNTIME)
            //@Test2
    @interface Test3 {
        /**
         * AliasFor注解用来表示要覆盖Test2注解中的test2()属性方法，
         * annotation属性声明的注解类必须存在于该注解的元注解上
         * attribute属性声明的值必须存在于Test2注解属性方法中(即Test2注解的test2方法)
         */
        @AliasFor(annotation = Test2.class, attribute = "test2")
        String test3() default "test3";
    }

    /**
     * 只有@Test3注解，但是Test3注解上组合了@Test2注解，并将该注解的test3方法值用来覆盖Test2注解中的test2方法
     * 即更低层次声明的覆盖规则，会覆盖更高层次的属性方法值，即调用高层次的注解方法值实际显示的是低层所赋的值
     * 当然也可以将组合注解作用于更高层次，如Test3组合Test2,Test2组合Test1，然后将Test3作用于元素，通过工具类获取Test1注解覆盖的属性值
     */
    @Test3(test3 = "覆盖Test1属性中的test1方法")
    static class Element {
    }

    @Test2(test2 = "覆盖Test1属性中的test1方法")
    static class Element2 {
    }

    public static void main(String[] args) {
        Test1 annotation = AnnotationUtils.getMergedAnnotation(Element.class, Test1.class);
        // 虽然调用了Test2注解的test2方法，但是实际显示的是Test3注解中的test3属性声明的值
        // 则说明Test2的test2属性被覆盖了
        Test1 annotation2 = AnnotationUtils.getMergedAnnotation(Element2.class, Test1.class);
        System.out.println(annotation.hashCode());
        System.out.println(annotation.toString());
        System.out.println(annotation.equals(annotation2));
        System.out.println(annotation.annotationType());
    }
}
