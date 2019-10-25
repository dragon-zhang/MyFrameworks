package ConcurrentProgram.basic;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author SuccessZhang
 * 线程在睡眠时其拥有的锁监视器资源不会被释放；
 * 而调用wait()方法时会释放锁监视器资源。
 */
public class SleepDemo {

    public static void main(String[] args) {
        Lock lock = new ReentrantLock();
        Thread thread1 = new Thread(() -> {
            try {
                lock.lock();
                System.out.println("thread1 start sleep");
                Thread.sleep(3000);
                System.out.println("thread1 awaked");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        });
        Thread thread2 = new Thread(() -> {
            try {
                lock.lock();
                System.out.println("thread2 start sleep");
                Thread.sleep(3000);
                System.out.println("thread2 awaked");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        });
        thread1.start();
        thread2.start();
    }
}
