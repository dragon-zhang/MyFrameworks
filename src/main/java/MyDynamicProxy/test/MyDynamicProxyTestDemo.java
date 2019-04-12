package MyDynamicProxy.test;

/**
 * @author SuccessZhang
 */
public class MyDynamicProxyTestDemo {

    public static void main(String[] args) {
        TestService service = TestProxy.getInstance(TestClass.class);
        service.test();
        System.out.println();
        service.test(1, "");
    }

}