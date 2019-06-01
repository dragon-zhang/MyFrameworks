package DesignPattern.factory.abstractFactory.impl;

import DesignPattern.factory.abstractFactory.FoodFactory;
import DesignPattern.factory.common.Cake;
import DesignPattern.factory.common.Fruit;
import DesignPattern.factory.common.Vegetable;
import DesignPattern.factory.common.impl.Apple;
import DesignPattern.factory.common.impl.Cabbage;
import DesignPattern.factory.common.impl.FruitCake;

/**
 * @author SuccessZhang
 */
public class FoodFactoryOne implements FoodFactory {
    @Override
    public Fruit createFruit() {
        return new Apple();
    }

    @Override
    public Cake createCake() {
        return new FruitCake();
    }

    @Override
    public Vegetable createVegetable() {
        return new Cabbage();
    }
}
