package ConcurrentProgram.advanced;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author SuccessZhang
 * 悲观锁：指对数据被外部修改，认为数据很容易被其他线程修改，所
 * 以在数据被处理之前先对数据进行加锁并在整个数据处理的过程中保
 * 持数据处于锁定的状态；
 * 乐观锁：相较于悲观锁来说，它认为数据在一般情况下不会造成冲突，
 * 所以在访问前不会加排他锁，而是在提交更新时正式对数据冲突与否
 * 进行检测。
 */
public class PessimisticLockAndOptimisticLockDemo {

    public static void main(String[] args) {
        PessimisticLockDemo();
        System.out.println("------------------------------------------");
        OptimisticLockDemo();
    }

    public static int integer = 0;

    public static synchronized void add(String name) {
        System.out.println(name + " get lock and add integer to " + (++integer));
    }

    private static void PessimisticLockDemo() {
        Thread thread1 = new Thread(() -> {
            add("thread1");
        });
        Thread thread2 = new Thread(() -> {
            add("thread2");
        });
        thread1.start();
        thread2.start();

        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("pessimistic lock demo is over");
    }

    private static void OptimisticLockDemo() {
        AtomicInteger integer = new AtomicInteger(0);
        Thread thread1 = new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + " try to update, " + (integer.compareAndSet(1, 1) ? "success" : "failure"));
        });
        Thread thread2 = new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + " try to update, " + (integer.compareAndSet(0, 1) ? "success" : "failure"));
        });
        thread1.start();
        thread2.start();

        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("optimistic lock demo is over");
    }

}
