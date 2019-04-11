package MyDynamicProxy.test;

/**
 * @author SuccessZhang
 */
public class MyDynamicProxyTestDemo {

    public static void main(String[] args) {
        Service service = TestProxy.getInstance(TestClass.class);
        service.test();
    }

}