package factory.abstractFactory.impl;

import factory.abstractFactory.FoodFactory;
import factory.common.Cake;
import factory.common.Fruit;
import factory.common.Vegetable;
import factory.common.impl.Apple;
import factory.common.impl.Cabbage;
import factory.common.impl.FruitCake;

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
