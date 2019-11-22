package basic;

/**
 * @author SuccessZhang
 * 不获取监视器锁就wait()、notify()，
 * 会抛出IllegalMonitorStateException异常
 * 详见{@link WaitDemo}。
 */
public class WaitNotifyDemo {

    private static volatile boolean flag = true;

    public static void main(String[] args) {
        Object lock = new Object();
        Thread thread1 = new Thread(() -> {
            try {
                while (true) {
                    Thread.sleep(1000);
                    synchronized (lock) {
                        lock.notify();
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
                    synchronized (lock) {
                        if (flag) {
                            System.out.println("wait");
                            flag = false;
                            lock.wait();
                        } else {
                            System.out.println("notify");
                            flag = true;
                        }
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
