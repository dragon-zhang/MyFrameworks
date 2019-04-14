package MyDynamicProxy.test;

import MySpringMVC.V2.aop.AOPMethods;

/**
 * @author SuccessZhang
 */
public class TestAOPClass implements TestService, AOPMethods {

    public TestAOPClass() {
    }

    /**
     * 一个模拟的add方法
     */
    @Override
    public Object test() {
        return 2;
    }

    @Override
    public Object test(int i, String j) {
        return null;
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