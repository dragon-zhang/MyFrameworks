package MySpringMVC.V2.aop.proxy.test;

/**
 * @author SuccessZhang
 * @date 2020/04/11
 */
public class MyProxyTestDemo {

    public static void main(String[] args) {
        System.out.println("cglib-----------------------------------------------------------------------------------------------------\n");
        TestServiceImpl test = TestProxy.getInstance(TestServiceImpl.class);
        System.out.println(test.test());
        System.out.println(test.test(1, "cglib"));

        System.out.println("\njdk-------------------------------------------------------------------------------------------------------\n");

        TestService testAop = TestProxy.getInstance(TestService.class);
        System.out.println(testAop.test());
        System.out.println(testAop.test(2, "jdk"));
    }

}