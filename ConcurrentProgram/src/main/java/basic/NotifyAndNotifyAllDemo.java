package basic;

/**
 * @author SuccessZhang
 * notify()只会随机唤醒一个被挂起的线程；
 * notifyAll()会唤醒所有被挂起的线程。
 */
public class NotifyAndNotifyAllDemo {

    private static volatile Object lock = new Object();

    /**
     * notify结果
     * thread1 get lock and begin wait
     * thread2 get lock and begin wait
     * thread3 begin notify
     * thread3 end notify
     * thread1 end wait
     * <p>
     * notifyAll结果
     * thread1 get lock and begin wait
     * thread2 get lock and begin wait
     * thread3 begin notify
     * thread3 end notify
     * thread2 end wait
     * thread1 end wait
     * over
     */
    public static void main(String[] args) throws InterruptedException {
        Thread thread1 = new Thread(() -> {
            synchronized (lock) {
                try {
                    System.out.println("thread1 get lock and begin wait");
                    lock.wait();
                    System.out.println("thread1 end wait");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        Thread thread2 = new Thread(() -> {
            synchronized (lock) {
                try {
                    System.out.println("thread2 get lock and begin wait");
                    lock.wait();
                    System.out.println("thread2 end wait");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        Thread thread3 = new Thread(() -> {
            synchronized (lock) {
                System.out.println("thread3 begin notify");
                lock.notifyAll();
                System.out.println("thread3 end notify");
            }
        });

        thread1.start();
        thread2.start();
        Thread.sleep(1000);
        thread3.start();

        thread1.join();
        thread2.join();
        thread3.join();

        System.out.println("main thread is over");
    }
}
