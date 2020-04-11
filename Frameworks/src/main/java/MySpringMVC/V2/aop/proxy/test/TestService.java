package MySpringMVC.V2.aop.proxy.test;

/**
 * @author SuccessZhang
 * @date 2020/04/11
 */
public interface TestService {
    Object test();

    /**
     * 测试重载的方法
     */
    Object test(int i, String j);
}
