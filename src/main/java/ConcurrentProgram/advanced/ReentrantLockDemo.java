package ConcurrentProgram.advanced;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author SuccessZhang
 * synchronized和Lock都属于可重入锁
 */
public class ReentrantLockDemo {

    private synchronized void test1() {
        System.out.println("test1");
        test2();
    }

    private void test2() {
        synchronized (this) {
            //体现了可重入性
            System.out.println("test2");
        }
    }

    private void test3(Lock lock) {
        lock.lock();
        System.out.println("test3");
        test4(lock);
        lock.unlock();
    }

    private void test4(Lock lock) {
        //体现了可重入性
        lock.lock();
        System.out.println("test4");
        lock.unlock();
    }

    public static void main(String[] args) {
        ReentrantLockDemo demo = new ReentrantLockDemo();
        demo.test1();
        Lock lock = new ReentrantLock();
        demo.test3(lock);
    }
}
