package DesignPattern.decorator.common;

/**
 * @author SuccessZhang
 */
public class Pancake {
    protected double price;
    protected String msg;

    public Pancake() {
        this.price = 5;
        msg = "当前有:1个煎饼";
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public double getPrice() {
        return price;
    }

    public String getMsg() {
        return msg;
    }
}
