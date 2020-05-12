package MySpringMVC.V2.aop.proxy.test;

/**
 * @author SuccessZhang
 * @date 2020/04/11
 */
public class TestServiceImpl implements TestService {

    public TestServiceImpl() {
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
        System.out.println("the i is [" + i + "] and the j is [" + j + "]");
        return null;
    }

    public void before() {
        System.out.println("cglib before");
    }

    public void aroundBefore() {
        System.out.println("cglib aroundBefore");
    }

    public void afterReturning(Object result) {
        System.out.println("cglib afterReturning:" + result);
    }

    public void afterThrowing(Throwable throwable) {
        System.out.println("cglib afterThrowing:" + throwable);
    }

    public void after() {
        System.out.println("cglib after");
    }

    public void aroundAfter() {
        System.out.println("cglib aroundAfter");
    }
}