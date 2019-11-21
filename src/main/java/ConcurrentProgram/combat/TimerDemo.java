package ConcurrentProgram.combat;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author SuccessZhang
 * {@link java.util.Timer}固定多线程生产、单线程消费；
 * 而{@link java.util.concurrent.ScheduledThreadPoolExecutor}可配置，
 * 既可以是多线程生产、单线程消费，也可以是多线程生产、多线程消费。
 */
public class TimerDemo {

    public static void main(String[] args) throws InterruptedException {
        ErrorDemo();
        Thread.sleep(2000);
        System.out.println("-----------------------------------------------");
        ScheduledExecutorServiceDemo();
    }

    /**
     * 当任务在执行任务的过程中抛出了
     * {@link java.lang.InterruptedException}以外的异常时，
     * 唯一的消费线程会终止，所以在使用
     * {@link java.util.TimerTask}时，需要使用try-catch来捕获异常。
     */
    private static void ErrorDemo() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println(Thread.currentThread().getName() + " task one");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                throw new RuntimeException("error");
            }
        }, 500);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println(Thread.currentThread().getName() + " task two");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                throw new RuntimeException("error");
            }
        }, 1000);
    }

    /**
     * ScheduledThreadPoolExecutor不会抛出异常是因为在
     * ScheduledFutureTask里捕获了异常。
     */
    private static void ScheduledExecutorServiceDemo() {
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(6);
        executor.schedule((Runnable) () -> {
            System.out.println(Thread.currentThread().getName() + " task one");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            throw new RuntimeException("error");
        }, 500, TimeUnit.MILLISECONDS);
        executor.schedule((Runnable) () -> {
            System.out.println(Thread.currentThread().getName() + " task two");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            throw new RuntimeException("error");
        }, 1000, TimeUnit.MILLISECONDS);
        executor.shutdown();
    }
}
