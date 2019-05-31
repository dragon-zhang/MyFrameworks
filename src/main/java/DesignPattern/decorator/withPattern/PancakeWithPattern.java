package DesignPattern.decorator.withPattern;

import DesignPattern.decorator.common.Egg;
import DesignPattern.decorator.common.Sausage;

/**
 * @author SuccessZhang
 * 为方便形成鲜明对比，这里PancakeWithPattern就不继承于Pancake
 */
public class PancakeWithPattern {

    private double price;

    private String msg;

    public PancakeWithPattern() {
        this.price = 5;
        msg = "当前有:1个煎饼";
    }

    public double getPrice() {
        return price;
    }

    public String getMsg() {
        return msg;
    }

    public void addEgg(Egg egg) {
        this.price = this.price + egg.getPrice();
        this.msg = this.msg + "+1个蛋";
    }

    public void addSausage(Sausage sausage) {
        this.price = this.price + sausage.getPrice();
        this.msg = this.msg + "+1根肠";
    }
}
