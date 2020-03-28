package MySpringMVC.V1.aop.test;

import MySpringMVC.V1.aop.annotation.After;
import MySpringMVC.V1.aop.annotation.AfterReturning;
import MySpringMVC.V1.aop.annotation.AfterThrowing;
import MySpringMVC.V1.aop.annotation.AroundAfter;
import MySpringMVC.V1.aop.annotation.AroundBefore;
import MySpringMVC.V1.aop.annotation.Before;

/**
 * @author SuccessZhang
 */
public class Test1 {

    public Test1() {
    }

    /**
     * 一个模拟的add方法
     */
    int add() {
        return 1;
    }

    @Before
    public void before() {
        System.out.println("before");
    }

    @AroundBefore
    public void aroundBefore() {
        System.out.println("aroundBefore");
    }

    @AfterReturning
    public void afterReturning(Object result) {
        System.out.println("afterReturning:" + result);
    }

    @AfterThrowing
    public void afterThrowing(Throwable throwable) {
        System.out.println("afterThrowing:" + throwable);
    }

    @After
    public void after() {
        System.out.println("after");
    }

    @AroundAfter
    public void aroundAfter() {
        System.out.println("aroundAfter");
    }
}