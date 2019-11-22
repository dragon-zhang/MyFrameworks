package proxy.test;

import proxy.Matchmaker;
import proxy.Person;
import proxy.impl.Man;

/**
 * @author SuccessZhang
 * 代理之后对目标对象的功能增强，
 * 能将代理对象与真实被调用的目标对象分离，亦可以起到保护目标对象的作用
 * 代理一定程度上降低了系统的耦合度，扩展性好
 * 缺点：增加了系统的复杂度；会造成请求处理速度变慢
 * 以下仅演示静态代理，动态代理请查看MyDynamicProxy
 */
public class ProxyTest {
    public static void main(String[] args) {
        Person man = new Man("张三");
        Matchmaker matchmaker = new Matchmaker();
        matchmaker.addSeeker(man);
        matchmaker.makeMatch();
    }
}
