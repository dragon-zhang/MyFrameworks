package ConcurrentProgram.advanced;

import java.util.concurrent.atomic.LongAdder;

/**
 * @author SuccessZhang
 * 在高并发的场景下使用原子类(如AtomicLong)时，大量线程会同时
 * 去竞争一个原子变量，但是只有一个线程会CAS成功，于是就造成了
 * 大量线程竞争失败，继续通过自旋不断尝试CAS，白白浪费CPU资源。
 * <p>
 * JDK8新增LongAdder来解决原子类的性能问题，其实现基于这样的思想：
 * 多个线程去竞争多个原子变量，对当前原子变量CAS失败后，尝试CAS其
 * 他原子变量。最后需要获取当前值时，将所有原子变量的值累加到base
 * 上，而后返回。
 */
public class LongAdderDemo {

    public static void main(String[] args) {
        Integer[] array1 = new Integer[]{0, 1, 2, 3, 0, 5, 6, 0, 56, 0};
        Integer[] array2 = new Integer[]{1, 1, 2, 3, 0, 5, 6, 0, 56, 0};
        LongAdder count = new LongAdder();
        Thread thread1 = new Thread(() -> {
            for (Integer integer : array1) {
                if (integer == 0) {
                    count.increment();
                }
            }
        });
        Thread thread2 = new Thread(() -> {
            for (Integer integer : array2) {
                if (integer == 0) {
                    count.increment();
                }
            }
        });
        thread1.start();
        thread2.start();

        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("zero count is " + count.sum());
    }

}
