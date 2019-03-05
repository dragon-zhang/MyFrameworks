package MySpringMVC;

import MySpringMVC.aop.AOPMethods;

/**
 * @author SuccessZhang
 */
public class Test2 implements Service, AOPMethods {

    public Test2() {
    }

    /**
     * 一个模拟的add方法
     */
    @Override
    public int add() {
        return 2;
    }

    @Override
    public void before() {
        System.out.println("before");
    }

    @Override
    public void aroundBefore() {
        System.out.println("aroundBefore");
    }

    @Override
    public void afterReturning(Object result) {
        System.out.println("afterReturning:" + result);
    }

    @Override
    public void afterThrowing(Throwable throwable) {
        System.out.println("afterThrowing:" + throwable);
    }

    @Override
    public void after() {
        System.out.println("after");
    }

    @Override
    public void aroundAfter() {
        System.out.println("aroundAfter");
    }
}