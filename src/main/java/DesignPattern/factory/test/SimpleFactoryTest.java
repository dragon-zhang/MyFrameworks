package DesignPattern.factory.test;

import DesignPattern.factory.common.Fruit;
import DesignPattern.factory.common.impl.Apple;
import DesignPattern.factory.common.impl.Banana;
import DesignPattern.factory.simpleFactory.SimpleFactory;

/**
 * @author SuccessZhang
 */
public class SimpleFactoryTest {
    public static void main(String[] args) {
        System.out.println(SimpleFactory.create(Apple.class));
        System.out.println(SimpleFactory.create(Banana.class));
        System.out.println(SimpleFactory.create(Fruit.class));
    }
}
