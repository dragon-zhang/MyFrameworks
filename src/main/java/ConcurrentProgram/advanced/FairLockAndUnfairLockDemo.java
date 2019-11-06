package ConcurrentProgram.advanced;

import java.util.concurrent.locks.ReentrantLock;

/**
 * @author SuccessZhang
 * 公平锁：获取锁的顺序由请求锁时间的早晚决定；
 * 非公平锁：先来不一定先得。
 */
public class FairLockAndUnfairLockDemo {

    public static void main(String[] args) {
        fairLockDemo();
        System.out.println("-----------------------------------------");
        unfairLockDemo();
    }

    private static void fairLockDemo() {
        common(new ReentrantLock(true));
    }

    private static void unfairLockDemo() {
        common(new ReentrantLock(false));
    }

    private static void common(ReentrantLock lock) {
        Runnable runnable = () -> {
            System.out.println("线程" + Thread.currentThread().getName() + "尝试获取锁");
            try {
                lock.lock();
                System.out.println(Thread.currentThread().getName() + "获得锁");
            } finally {
                lock.unlock();
            }
        };
        Thread[] threadArray = new Thread[10];
        for (int i = 0; i < threadArray.length; i++) {
            threadArray[i] = new Thread(runnable);
        }
        for (Thread thread : threadArray) {
            thread.start();
        }
        for (Thread thread : threadArray) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println((lock.isFair() ? "fair" : "unfair") + " lock demo is over");
    }
}
