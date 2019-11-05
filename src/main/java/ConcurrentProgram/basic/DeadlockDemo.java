package ConcurrentProgram.basic;

/**
 * @author SuccessZhang
 * 死锁产生的必备条件:
 * 1.互斥条件;
 * 2.请求并持有条件;
 * 3.不可剥夺条件;
 * 4.环路等待条件.
 */
public class DeadlockDemo {

    public static void main(String[] args) throws InterruptedException {
        Object lock1 = new Object();
        Object lock2 = new Object();
        Thread thread1 = new Thread(() -> {
            synchronized (lock1) {
                System.out.println(Thread.currentThread() + " get lock1");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread() + " need lock2");
                synchronized (lock2) {
                    System.out.println(Thread.currentThread() + " get lock2");
                }
            }
        });
        Thread thread2 = new Thread(() -> {
            synchronized (lock2) {
                System.out.println(Thread.currentThread() + " get lock2");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread() + " need lock1");
                synchronized (lock1) {
                    System.out.println(Thread.currentThread() + " get lock1");
                }
            }
        });
        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();

        System.out.println("main thread is over");
    }

}
