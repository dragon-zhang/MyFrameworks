package ConcurrentProgram.advanced;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author SuccessZhang
 * 这里用Condition的await()、signal()替代wait()、notify()，
 * 原先用synchronized同时只能与一个共享变量实现同步，
 * 而AQS的一个锁可以同时对应多个条件变量；
 * 不获取监视器锁就await()、signal()，
 * 会抛出{@link java.lang.IllegalMonitorStateException}异常，
 * 详见{@link ConcurrentProgram.advanced.AdvancedWaitDemo}。
 */
public class AdvancedWaitNotifyDemo {

    private static volatile boolean flag = true;

    public static void main(String[] args) {
        ReentrantLock lock = new ReentrantLock();
        Condition condition = lock.newCondition();
        Thread thread1 = new Thread(() -> {
            try {
                while (true) {
                    Thread.sleep(1000);
                    try {
                        lock.lock();
                        condition.signal();
                    } finally {
                        lock.unlock();
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        Thread thread2 = new Thread(() -> {
            System.out.println("waitWithLock started");
            try {
                while (true) {
                    Thread.sleep(1000);
                    try {
                        lock.lock();
                        if (flag) {
                            System.out.println("wait");
                            flag = false;
                            condition.await();
                        } else {
                            System.out.println("notify");
                            flag = true;
                        }
                    } finally {
                        lock.unlock();
                    }
                }
            } catch (InterruptedException e) {
                System.out.println("waitWithLock InterruptedException");
            }
            System.out.println("waitWithLock finished");
        });
        thread1.start();
        thread2.start();
    }
}
