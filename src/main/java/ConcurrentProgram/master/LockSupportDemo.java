package ConcurrentProgram.master;

import java.util.concurrent.locks.LockSupport;

/**
 * @author SuccessZhang
 * 默认情况下调用LockSupport类方法的线程是不持有许可证的。
 */
public class LockSupportDemo {

    /**
     * 使某线程获得许可证
     *
     * @param thread 需要获得LockSupport许可证的线程
     */
    public static void getLicense(Thread thread) {
        LockSupport.unpark(thread);
    }

    public static void main(String[] args) {
        /*
         * 如果线程已经拿到了许可证，则调用park()会马上返回；
         * 否则该线程会被阻塞挂起。
         */
        getLicense(Thread.currentThread());
        System.out.println("main thread begin park");
        LockSupport.park();
        System.out.println("main thread had parked");

        Thread thread = new Thread(() -> {
            System.out.println("thread begin park");
            LockSupport.park();
            System.out.println("thread unpark");
        });
        thread.start();

        try {
            Thread.sleep(1000);
            System.out.println("thread get license or been interrupted");
            getLicense(thread);
            /*
             * 无论是unpark()还是interrupt()都会导致park()马上返回；
             * 因此如果是为了保证park()只因为一种原因返回，建议使用
             * while(条件){LockSupport.park();}
             */
            //thread.interrupt();
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("main thread is over");
    }

}
