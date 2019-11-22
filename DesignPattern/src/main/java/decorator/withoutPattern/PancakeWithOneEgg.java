package decorator.withoutPattern;

import decorator.common.Egg;
import decorator.common.Pancake;

/**
 * @author SuccessZhang
 */
public class PancakeWithOneEgg extends Pancake {

    public PancakeWithOneEgg(Pancake pancake) {
        super();
        Egg egg = new Egg();
        this.price = pancake.getPrice() + egg.getPrice();
        this.msg = pancake.getMsg() + "+1个蛋";
    }
}
