package DesignPattern.factory.factoryMethod.impl;

import DesignPattern.factory.common.Fruit;
import DesignPattern.factory.common.impl.Banana;
import DesignPattern.factory.factoryMethod.FruitFactory;

/**
 * @author SuccessZhang
 */
public class BananaFactory implements FruitFactory {
    @Override
    public Fruit create() {
        return new Banana();
    }
}
