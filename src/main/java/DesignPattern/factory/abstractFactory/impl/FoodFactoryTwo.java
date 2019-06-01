package DesignPattern.factory.abstractFactory.impl;

import DesignPattern.factory.abstractFactory.FoodFactory;
import DesignPattern.factory.common.Cake;
import DesignPattern.factory.common.Fruit;
import DesignPattern.factory.common.Vegetable;
import DesignPattern.factory.common.impl.Banana;
import DesignPattern.factory.common.impl.Porphyra;
import DesignPattern.factory.common.impl.VegetableCake;

/**
 * @author SuccessZhang
 */
public class FoodFactoryTwo implements FoodFactory {
    @Override
    public Fruit createFruit() {
        return new Banana();
    }

    @Override
    public Cake createCake() {
        return new VegetableCake();
    }

    @Override
    public Vegetable createVegetable() {
        return new Porphyra();
    }
}
