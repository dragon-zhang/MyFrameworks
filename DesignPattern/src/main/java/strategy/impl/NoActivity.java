package strategy.impl;

import strategy.Activity;

/**
 * @author SuccessZhang
 */
public class NoActivity implements Activity {
    @Override
    public String getName() {
        return "无活动";
    }

    @Override
    public double getFinalPrice(double price) {
        return price;
    }

}
