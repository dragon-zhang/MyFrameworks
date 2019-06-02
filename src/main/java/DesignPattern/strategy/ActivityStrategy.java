package DesignPattern.strategy;

import DesignPattern.strategy.impl.DiscountActivity;
import DesignPattern.strategy.impl.FullScaleActivity;
import DesignPattern.strategy.impl.NoActivity;

import java.util.HashMap;
import java.util.Map;

/**
 * @author SuccessZhang
 * 策略模式符合开闭原则，也避免了使用多重条件转移语句
 * 但是客户端必须知道所有的策略，并且自行决定使用哪一个策略类；
 * 而且代码中会产生非常多策略类，增加了维护难度
 */
public class ActivityStrategy {

    private static Map<Strategy, Activity> strategies = new HashMap<>();

    static {
        strategies.put(Strategy.DISCOUNT, new DiscountActivity());
        strategies.put(Strategy.FULL_SCALE, new FullScaleActivity());
        strategies.put(Strategy.DEFAULT, new NoActivity());
    }

    public static double startActivity(Strategy strategy, double price) {
        if (strategy == null) {
            strategy = Strategy.DEFAULT;
        }
        return strategies.get(strategy).getFinalPrice(price);
    }

    public enum Strategy {
        //打折
        DISCOUNT,
        //满减
        FULL_SCALE,
        //默认
        DEFAULT
    }
}
