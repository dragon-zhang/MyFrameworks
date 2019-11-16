package ConcurrentProgram.advanced;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * @author SuccessZhang
 * 线程池的种类：
 * 1.Executors.newSingleThreadExecutor();
 * 创建一个核心线程数和最大线程数都为1的线程池，
 * 阻塞队列长度为Integer.MAX_VALUE，
 * 只要线程个数超过核心线程数并且当前空闲就回收。
 * <p>
 * 2.Executors.newFixedThreadPool(n);
 * 创建一个核心线程数和最大线程数都为n的线程池，
 * 阻塞队列长度为Integer.MAX_VALUE，
 * 只要线程个数超过核心线程数并且当前空闲就回收。
 * <p>
 * 3.Executors.newCachedThreadPool();
 * 创建一个按需创建线程的线程池，
 * 初始线程个数为0，最大线程数为Integer.MAX_VALUE，
 * 阻塞队列为同步队列，加入同步队列的任务马上会被执行，
 * 但是同步队列里最多只有一个任务，
 * 只要当前线程在60s内空闲就回收。
 * <p>
 * 4.Executors.newScheduledThreadPool(n);
 * 创建一个可以指定一定延迟时间后或者定时进行任务调度的线程池
 * (只有当任务的执行时间到来时，ScheduedExecutor才会真正启动
 * 一个线程，其余时间ScheduledExecutor都是在轮询任务的状态)，
 * 核心线程数为n，最大线程数为Integer.MAX_VALUE，
 * 阻塞队列为可延时执行阻塞任务的队列，最大长度为Integer.MAX_VALUE，
 * 只要线程个数超过核心线程数并且当前空闲就回收。
 * 以下方法适用于不同的业务场景：
 * schedule()延迟一定时间后执行一次任务；
 * scheduleAtFixedRate()按照固定的频率多次执行同一任务；
 * scheduleWithFixedDelay()同一任务在多次执行之间的间隔时间固定。
 * <p>
 * 但是上述4种线程池，底层调用的都是同一个构造方法
 * {@link java.util.concurrent.ThreadPoolExecutor 1197行}。
 * ThreadPoolExecutor本质上是一个生产者-消费者模型，
 * 用户添加任务到线程池相当于生产者生产元素，
 * workers线程工作集中的线程直接执行任务或者从任务队列里获取任务，
 * 相当于消费者消费元素。
 */
public class ThreadPoolDemo {

    public static class MyThreadFactory implements ThreadFactory {

        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r);
        }

    }

    public static class MyRejectedExecutionHandler implements RejectedExecutionHandler {

        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
            if (executor.isShutdown()) {
                //线程池没有被关闭
                throw new RejectedExecutionException("ThreadPool is shutdown");
            } else {
                //任务数量超过最大值（队列长度 + maximumPoolSize）
                throw new RejectedExecutionException("the number of tasks exceeds the maximum");
            }
        }

    }

    private static ExecutorService executor = new ThreadPoolExecutor(
            6,
            12,
            5,
            TimeUnit.MINUTES,
            new LinkedBlockingQueue<>(10),
            new MyThreadFactory(),
            new MyRejectedExecutionHandler());

    public static void main(String[] args) {
        List<Future<String>> result = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            int temp = i;
            try {
                result.add(executor.submit(new Callable<String>() {
                    @Override
                    public String call() throws Exception {
                        System.out.println(Thread.currentThread().getName() + " says " + temp);
                        return String.valueOf(temp);
                    }
                }));
            } catch (RejectedExecutionException e) {
                System.out.println(e.getMessage());
            }
        }
        for (Future<String> future : result) {
            try {
                System.out.println(Thread.currentThread().getName() + " " + future.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        executor.shutdown();
    }

}
