package MyDynamicProxy.test;

/**
 * @author SuccessZhang
 */
public class MyDynamicProxyTestDemo {

    public static void main(String[] args) {
        TestClass test = TestProxy.getInstance(TestClass.class);
        test.test();
        System.out.println();
        test.test(1, "");

        System.out.println("\n-------------------------------------------------------------------------------------------------------\n");

        TestService testAop = TestProxy.getInstance(TestAOPClass.class);
        testAop.test();
        System.out.println();
        testAop.test(1, "");
    }

}