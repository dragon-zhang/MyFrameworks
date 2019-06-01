package DesignPattern.factory.test;

import DesignPattern.factory.abstractFactory.FoodFactory;
import DesignPattern.factory.abstractFactory.impl.FoodFactoryOne;
import DesignPattern.factory.abstractFactory.impl.FoodFactoryTwo;

/**
 * @author SuccessZhang
 * 不同的工厂创建多元不同产品
 */
public class AbstractFactoryTest {
    public static void main(String[] args) {
        FoodFactory factory1 = new FoodFactoryOne();
        System.out.println(factory1.createCake());
        System.out.println(factory1.createFruit());
        System.out.println(factory1.createVegetable());
        FoodFactory factory2 = new FoodFactoryTwo();
        System.out.println(factory2.createCake());
        System.out.println(factory2.createFruit());
        System.out.println(factory2.createVegetable());
    }
}
