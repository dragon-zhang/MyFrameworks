package DesignPattern.decorator.withoutPattern;

import DesignPattern.decorator.common.Egg;

/**
 * @author SuccessZhang
 */
public class PancakeWithTwoEggs extends PancakeWithOneEgg {

    public PancakeWithTwoEggs(PancakeWithOneEgg pancakeWithOneEgg) {
        super(pancakeWithOneEgg);
        Egg egg = new Egg();
        this.price = pancakeWithOneEgg.getPrice() + egg.getPrice();
        this.msg = pancakeWithOneEgg.getMsg() + "+1个蛋";
    }
}
