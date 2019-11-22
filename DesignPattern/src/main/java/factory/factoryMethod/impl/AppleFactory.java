package factory.factoryMethod.impl;

import factory.common.Fruit;
import factory.common.impl.Apple;
import factory.factoryMethod.FruitFactory;

/**
 * @author SuccessZhang
 */
public class AppleFactory implements FruitFactory {
    @Override
    public Fruit create() {
        return new Apple();
    }
}
