package MyDynamicProxy.test;

/**
 * @author SuccessZhang
 */
public class TestClass implements TestService {

    public TestClass() {
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

    public void before() {
        System.out.println("before");
    }

    public void aroundBefore() {
        System.out.println("aroundBefore");
    }

    public void afterReturning(Object result) {
        System.out.println("afterReturning:" + result);
    }

    public void afterThrowing(Throwable throwable) {
        System.out.println("afterThrowing:" + throwable);
    }

    public void after() {
        System.out.println("after");
    }

    public void aroundAfter() {
        System.out.println("aroundAfter");
    }
}