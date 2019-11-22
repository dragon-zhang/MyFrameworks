package decorator.withPattern;

import decorator.common.Egg;
import decorator.common.Sausage;

/**
 * @author SuccessZhang
 * 为方便形成鲜明对比，这里PancakeWithPattern就不继承于Pancake
 * 要点：使用对象组合(has-a)/聚合(contanis-a)，而不是继承关系达到软件复用的目的
 * 满足合成复用原则，如果扩展不多，则大大增加了系统的复杂性；
 * 继承叫做白箱复用，相当于把所有的实现细节暴露给子类；
 * 组合/聚合称之为黑箱复用，对类以外的对象是无法获取到实现细节的。
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
