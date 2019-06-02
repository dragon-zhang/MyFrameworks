package DesignPattern.strategy.impl;

import DesignPattern.strategy.Activity;

/**
 * @author SuccessZhang
 */
public class FullScaleActivity implements Activity {
    @Override
    public String getName() {
        return "满100减30";
    }

    @Override
    public double getFinalPrice(double price) {
        if (price >= 100) {
            return price - 30;
        }
        return price;
    }

}
