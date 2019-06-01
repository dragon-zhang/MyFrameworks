package DesignPattern.factory.simpleFactory;

import DesignPattern.factory.common.Fruit;

/**
 * @author SuccessZhang
 * 简单工厂，后续可用策略模式简化if-else或者switch
 * 违反开闭原则、单一职责原则
 */
public class SimpleFactory {
    public static Fruit create(Class<? extends Fruit> type) {
        if (type != null && !type.isInterface()) {
            try {
                return type.newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
