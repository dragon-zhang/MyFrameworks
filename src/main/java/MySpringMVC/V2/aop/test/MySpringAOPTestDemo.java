package MySpringMVC.V2.aop.test;

import MySpringMVC.V2.aop.AOPProxy;

import java.util.HashMap;
import java.util.Map;

/**
 * @author SuccessZhang
 */
public class MySpringAOPTestDemo {

    public static void main(String[] args) {
        Test1 base = AOPProxy.getInstance(Test1.class);
        Map<String, Object> map = new HashMap<>(1);
        map.put("base", base);
        ((Test1) map.get("base")).add();
        System.out.println("------------------------------------");
        Service aop1 = AOPProxy.getInstance(Test2.class);
        aop1.add();
    }

}