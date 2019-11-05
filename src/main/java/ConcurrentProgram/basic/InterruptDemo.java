package ConcurrentProgram.basic;

/**
 * @author SuccessZhang
 * interrupt()仅仅是设置中断标志为true，
 * 线程实际并没有中断；
 * 线程在被阻塞挂起的状态下，如果其他线程调用
 * 了该线程的interrupt()方法，该线程会恢复到激活状态
 * 并在调用这些方法的地方抛出InterruptedException
 */
public class InterruptDemo {

    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(() -> {
            //如果当前线程被中断则退出循环
            while (!Thread.currentThread().isInterrupted()) {
                System.out.println(Thread.currentThread() + " says hello");
            }
        });
        thread.start();
        //中断前输出
        Thread.sleep(100);
        //中断线程，强制线程恢复到激活状态
        System.out.println("main thread interrupt thread");
        if (condition()) {
            thread.interrupt();
        }
        //等待子线程执行完毕
        thread.join();
        System.out.println("main thread over");
    }

    private static boolean condition() {
        //...条件
        return true;
    }

}
