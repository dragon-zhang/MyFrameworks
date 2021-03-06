package decorator.test;

import decorator.common.Egg;
import decorator.common.Pancake;
import decorator.common.Sausage;
import decorator.withPattern.PancakeWithPattern;
import decorator.withoutPattern.PancakeWithOneEgg;
import decorator.withoutPattern.PancakeWithTwoEggs;
import decorator.withoutPattern.PancakeWithTwoEggsAndOneSausage;
import decorator.withoutPattern.PancakeWithTwoEggsAndTwoSausages;

/**
 * @author SuccessZhang
 * 业务场景：计算加肠加蛋之后的煎饼的价格
 */
public class DecoratorPatternTest {
    public static void main(String[] args) {
        //加两根肠、两个蛋，不用设计模式的写法
        Pancake pancakeWithTwoEggsAndTwoSausages =
                new PancakeWithTwoEggsAndTwoSausages(
                        new PancakeWithTwoEggsAndOneSausage(
                                new PancakeWithTwoEggs(
                                        new PancakeWithOneEgg(
                                                new Pancake()))));
        System.out.println(pancakeWithTwoEggsAndTwoSausages.getMsg() + ",煎饼卖" + pancakeWithTwoEggsAndTwoSausages.getPrice() + "元");
        //加两根肠、两个蛋，较为优雅的写法
        PancakeWithPattern pancakeWithPattern = new PancakeWithPattern();
        pancakeWithPattern.addEgg(new Egg());
        pancakeWithPattern.addEgg(new Egg());
        pancakeWithPattern.addSausage(new Sausage());
        pancakeWithPattern.addSausage(new Sausage());
        System.out.println(pancakeWithPattern.getMsg() + ",煎饼卖" + pancakeWithPattern.getPrice() + "元");
    }
}
