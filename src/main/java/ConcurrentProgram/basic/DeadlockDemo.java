package ConcurrentProgram.basic;

/**
 * @author SuccessZhang
 * 死锁产生的必备条件:
 * 1.互斥条件(该资源同时只有一个线程占用);
 * 2.请求并持有条件(一个线程已经持有了至少一个资源，又提出了新的资源请求
 * ，而新资源已被其他线程占有);
 * 3.不可剥夺条件(线程获取到的资源在自己使用完之前不能被其他线程抢占);
 * 4.环路等待条件(发生死锁时必然存在一个线程-资源的环形链).
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
