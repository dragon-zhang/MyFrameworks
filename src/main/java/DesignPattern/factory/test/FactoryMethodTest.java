package DesignPattern.factory.test;

import DesignPattern.factory.factoryMethod.impl.AppleFactory;
import DesignPattern.factory.factoryMethod.impl.BananaFactory;

/**
 * @author SuccessZhang
 * 不同的工厂创建单一的不同产品
 */
public class FactoryMethodTest {
    public static void main(String[] args) {
        System.out.println(new AppleFactory().create());
        System.out.println(new BananaFactory().create());
    }
}
