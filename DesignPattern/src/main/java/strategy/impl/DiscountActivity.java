package strategy.impl;

import strategy.Activity;

/**
 * @author SuccessZhang
 */
public class DiscountActivity implements Activity {
    @Override
    public String getName() {
        return "全场9折";
    }

    @Override
    public double getFinalPrice(double price) {
        return price * 0.9;
    }

}
