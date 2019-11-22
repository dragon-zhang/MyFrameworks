package factory.factoryMethod.impl;

import factory.common.Fruit;
import factory.common.impl.Banana;
import factory.factoryMethod.FruitFactory;

/**
 * @author SuccessZhang
 */
public class BananaFactory implements FruitFactory {
    @Override
    public Fruit create() {
        return new Banana();
    }
}
