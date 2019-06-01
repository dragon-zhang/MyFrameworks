package DesignPattern.factory.factoryMethod.impl;

import DesignPattern.factory.common.Fruit;
import DesignPattern.factory.common.impl.Apple;
import DesignPattern.factory.factoryMethod.FruitFactory;

/**
 * @author SuccessZhang
 */
public class AppleFactory implements FruitFactory {
    @Override
    public Fruit create() {
        return new Apple();
    }
}
