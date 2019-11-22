package factory.abstractFactory;

import factory.common.Cake;
import factory.common.Fruit;
import factory.common.Vegetable;

/**
 * @author SuccessZhang
 * 不符合开闭原则
 */
public interface FoodFactory {
    Fruit createFruit();

    Cake createCake();

    Vegetable createVegetable();
}
