package basic;

/**
 * @author SuccessZhang
 * 破坏产生死锁的条件:
 * 1.破坏请求并持有条件;
 * 2.破坏环路等待条件.
 * <p>
 * 下面的做法是破坏环路等待条件.
 */
public class DeadlockBreakDemo {

    public static void main(String[] args) throws InterruptedException {
        Object lock1 = new Object();
        Object lock2 = new Object();
        Runnable runnable = () -> {
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
        };
        Thread thread1 = new Thread(runnable);
        Thread thread2 = new Thread(runnable);
        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();

        System.out.println("main thread is over");
    }

}
