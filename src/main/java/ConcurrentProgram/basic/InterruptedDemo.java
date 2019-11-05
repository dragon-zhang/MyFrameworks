package ConcurrentProgram.basic;

/**
 * @author SuccessZhang
 * Thread.interrupted()：检查当前线程是否被中断，
 * 如果是，清除中断标记并返回true(isInterrupted()不会清除中断标记)；
 * 否则，直接返回false
 */
public class InterruptedDemo {

    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(() -> {
            //中断标记为true时退出循环并且清除中断标记
            while (!Thread.interrupted()) {
                System.out.println(Thread.currentThread() + " is alive");
            }
            System.out.println("thread isInterrupted:" + Thread.currentThread().isInterrupted());
        });
        thread.start();
        //设置中断标记
        Thread.sleep(1);
        thread.interrupt();
        thread.join();
        System.out.println("main thread is over");
    }

}
