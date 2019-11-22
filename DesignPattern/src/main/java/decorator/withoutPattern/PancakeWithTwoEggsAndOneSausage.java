package decorator.withoutPattern;

import decorator.common.Sausage;

/**
 * @author SuccessZhang
 */
public class PancakeWithTwoEggsAndOneSausage extends PancakeWithTwoEggs {

    public PancakeWithTwoEggsAndOneSausage(PancakeWithTwoEggs pancakeWithTwoEggs) {
        super(pancakeWithTwoEggs);
        Sausage sausage = new Sausage();
        this.price = pancakeWithTwoEggs.getPrice() + sausage.getPrice();
        this.msg = pancakeWithTwoEggs.getMsg() + "+1根肠";
    }
}
