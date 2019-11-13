package ConcurrentProgram.master;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.StampedLock;

/**
 * @author SuccessZhang
 * StampedLock提供了三种锁(悲观写锁，乐观读锁，悲观读锁)，
 * 并且支持锁在一定条件下进行相互转换。
 * <p>
 * 成功升级写锁的条件(满足任意一个即可)：
 * 1.当前锁本身就是写锁；
 * 2.当前锁处于悲观读锁模式，并且没有其他线程是读锁模式；
 * 3.当前锁处于乐观读锁模式，并且当前写锁可用。
 */
public class StampedLockDemo {

    private int value = 0;

    private final StampedLock lock = new StampedLock();

    /**
     * 写锁。
     */
    public void update(int value) {
        long stamp = 0;
        try {
            stamp = lock.writeLock();
            this.value = value;
        } finally {
            lock.unlockWrite(stamp);
        }
    }

    /**
     * 尝试乐观读锁，不行再升级到悲观读锁。
     */
    public int get() {
        long stamp = lock.tryOptimisticRead();
        int current = this.value;
        if (!lock.validate(stamp)) {
            //版本失效
            try {
                stamp = lock.readLock();
                System.out.println(Thread.currentThread().getName() + " " +
                        current + " is expired, and the latest value is " +
                        this.value);
                return this.value;
            } finally {
                lock.unlockRead(stamp);
            }
        }
        System.out.println(Thread.currentThread().getName() +
                " " + current + " is not expired");
        return current;
    }

    /**
     * CAS
     */
    public void compareAndSet(int expect, int update) {
        long stamp = lock.readLock();
        try {
            while (true) {
                long temp = lock.tryConvertToWriteLock(stamp);
                if (temp == 0) {
                    //升级失败
                    System.out.println(Thread.currentThread().getName() + " try convert to write lock failed");
                    lock.unlockRead(stamp);
                    stamp = lock.writeLock();
                } else {
                    //升级成功
                    System.out.println(Thread.currentThread().getName() + " try convert to write lock success");
                    stamp = temp;
                    break;
                }
            }
            if (value == expect) {
                value = update;
                System.out.println(Thread.currentThread().getName() + " cas success");
            }
        } finally {
            lock.unlock(stamp);
        }
    }

    public static void main(String[] args) {
        StampedLockDemo demo = new StampedLockDemo();
        List<Thread> threads = new ArrayList<>();
        //15个写线程
        for (int i = 0; i < 15; i++) {
            threads.add(new Thread(() -> {
                int latest = demo.get();
                demo.compareAndSet(latest, latest + 1);
            }));
        }
        //35个读线程
        for (int i = 0; i < 35; i++) {
            threads.add(new Thread(() -> {
                demo.get();
            }));
        }
        for (Thread thread : threads) {
            thread.start();
        }
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("main thread is over, the final value is " + demo.get());
    }

}
