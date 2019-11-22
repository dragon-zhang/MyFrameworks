package factory.test;

import factory.common.Fruit;
import factory.common.impl.Apple;
import factory.common.impl.Banana;
import factory.simpleFactory.SimpleFactory;

/**
 * @author SuccessZhang
 */
public class SimpleFactoryTest {
    public static void main(String[] args) {
        System.out.println(SimpleFactory.create(Apple.class));
        System.out.println(SimpleFactory.create(Banana.class));
        System.out.println(SimpleFactory.create(Fruit.class));
    }
}
