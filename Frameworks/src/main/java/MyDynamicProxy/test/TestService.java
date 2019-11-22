package MyDynamicProxy.test;

/**
 * @author SuccessZhang
 */
public interface TestService {
    Object test();

    /**
     * 测试重载的方法
     */
    Object test(int i, String j);
}
