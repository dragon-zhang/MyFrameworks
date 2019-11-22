package factory.abstractFactory.impl;

import factory.abstractFactory.FoodFactory;
import factory.common.Cake;
import factory.common.Fruit;
import factory.common.Vegetable;
import factory.common.impl.Banana;
import factory.common.impl.Porphyra;
import factory.common.impl.VegetableCake;

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
