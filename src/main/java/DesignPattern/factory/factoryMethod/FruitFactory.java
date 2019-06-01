package DesignPattern.factory.factoryMethod;

import DesignPattern.factory.common.Fruit;

/**
 * @author SuccessZhang
 * 符合开闭原则、最少知道原则、依赖倒置原则、里氏替换原则
 * 但是增加了系统的复杂度及理解难度
 */
public interface FruitFactory {
    Fruit create();
}
