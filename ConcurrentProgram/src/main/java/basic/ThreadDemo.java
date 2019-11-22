package basic;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * @author SuccessZhang
 * 实现线程的三种方法。
 */
public class ThreadDemo {

    /**
     * 缺点：单继承
     */
    public static class MyThread extends Thread {
        @Override
        public void run() {
            System.out.println("hello Thread");
            //可通过this关键字直接获取当前线程
            System.out.println(this);
        }
    }

    /**
     * 缺点：无返回值
     */
    public static class MyRunnable implements Runnable {
        @Override
        public void run() {
            System.out.println("hello Runnable");
            //可通过Thread.currentThread()获取当前线程
            System.out.println(Thread.currentThread());
        }
    }

    /**
     * 异步化
     */
    public static class MyCallableTask implements Callable<String> {
        @Override
        public String call() throws Exception {
            System.out.println(Thread.currentThread());
            return "hello Callable";
        }
    }

    public static void main(String[] args) {
        //同步任务
        new MyThread().start();
        new Thread(new MyRunnable()).start();
        new Thread(new MyRunnable()).start();
        //异步任务，有返回值
        FutureTask<String> task = new FutureTask<>(new MyCallableTask());
        new Thread(task).start();
        try {
            System.out.println(task.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
}
