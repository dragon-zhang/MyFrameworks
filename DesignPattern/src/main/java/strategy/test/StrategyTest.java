package strategy.test;

import strategy.ActivityStrategy;

/**
 * @author SuccessZhang
 * 业务场景：店长决定开展活动
 */
public class StrategyTest {
    public static void main(String[] args) {
        System.out.println(ActivityStrategy.startActivity(null, 100));
        System.out.println(ActivityStrategy.startActivity(ActivityStrategy.Strategy.FULL_SCALE, 100));
        System.out.println(ActivityStrategy.startActivity(ActivityStrategy.Strategy.DISCOUNT, 100));
    }
}
