package advanced;

import java.util.concurrent.atomic.LongAccumulator;

/**
 * @author SuccessZhang
 * LongAdder实际上是LongAccumulator的一个特例，相当于@<code>
 * LongAdder count = new LongAdder();
 * 与下面的代码等价
 * LongAccumulator count = new LongAccumulator((left, right) -> left + right, 0);
 * </code>
 * LongAccumulator相较于LongAdder提供了更为强大的功能，
 * 可以让用户自定义计算规则和初始值。
 */
public class LongAccumulatorDemo {

    public static void main(String[] args) {
        LongAccumulator accumulator = new LongAccumulator((left, right) -> left * right, 1);
        accumulator.accumulate(5);
        accumulator.accumulate(5);
        System.out.println(accumulator.get());
    }

}
