package DesignPattern.factory.abstractFactory;

import DesignPattern.factory.common.Cake;
import DesignPattern.factory.common.Fruit;
import DesignPattern.factory.common.Vegetable;

/**
 * @author SuccessZhang
 * 不符合开闭原则
 */
public interface FoodFactory {
    Fruit createFruit();

    Cake createCake();

    Vegetable createVegetable();
}
