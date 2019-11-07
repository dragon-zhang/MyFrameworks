package ConcurrentProgram.advanced;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author SuccessZhang
 * 当多线程同时使用同单Random实例时，多线程会竞争同一个原子变量的CAS操作，
 * 但只有一个线程会成功，所以便造成大量线程自旋重试，降低并发性能。
 * 于是，ThreadLocalRandom应运而生。
 * ThreadLocalRandom操作的原子变量不再来源于实例本身，而是来自于ThreadLocal，
 * 也就是说，每个线程操作的实际上是本地线程内存里的副本。
 */
public class ThreadLocalRandomDemo {

    public static void main(String[] args) {
        Random random = new Random();
        ThreadLocalRandom threadLocalRandom = ThreadLocalRandom.current();
        long start = System.currentTimeMillis();
        for (int i = 0; i < 100; i++) {
            new Thread(() -> {
                for (int j = 0; j < 50; j++) {
                    random.nextInt(100);
                }
            }).start();
        }
        long end = System.currentTimeMillis();
        System.out.println(end - start);
        for (int i = 0; i < 100; i++) {
            new Thread(() -> {
                for (int j = 0; j < 50; j++) {
                    threadLocalRandom.nextInt(100);
                }
            }).start();
        }
        long end2 = System.currentTimeMillis();
        System.out.println(end2 - end);
    }

}
