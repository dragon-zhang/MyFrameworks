package DesignPattern.decorator.withoutPattern;

import DesignPattern.decorator.common.Sausage;

/**
 * @author SuccessZhang
 */
public class PancakeWithTwoEggsAndTwoSausages extends PancakeWithTwoEggsAndOneSausage {

    public PancakeWithTwoEggsAndTwoSausages(PancakeWithTwoEggsAndOneSausage pancakeWithTwoEggsAndOneSausage) {
        super(pancakeWithTwoEggsAndOneSausage);
        Sausage sausage = new Sausage();
        this.price = pancakeWithTwoEggsAndOneSausage.getPrice() + sausage.getPrice();
        this.msg = pancakeWithTwoEggsAndOneSausage.getMsg() + "+1根肠";
    }
}
