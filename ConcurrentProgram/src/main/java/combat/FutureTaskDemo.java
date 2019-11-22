package combat;

import java.lang.reflect.Field;
import java.util.concurrent.*;

/**
 * @author SuccessZhang
 * 在线程池中使用FutureTask时，当拒绝策略为
 * {@link ThreadPoolExecutor.DiscardPolicy}或
 * {@link ThreadPoolExecutor.DiscardOldestPolicy}时，
 * 在被拒绝任务的FutureTask对象上调用get()会导致调用线程一直阻塞，
 * 所以在开发中推荐使用get(long timeout, TimeUnit unit)以避免线程
 * 一直阻塞。
 */
public class FutureTaskDemo {

    public static class MyRejectedPolicy implements RejectedExecutionHandler {

        @Override
        public void rejectedExecution(Runnable runnable, ThreadPoolExecutor executor) {
            if (!executor.isShutdown()) {
                //线程池没有被关闭
                if (runnable instanceof FutureTask) {
                    FutureTask task = ((FutureTask) runnable);
                    try {
                        Field state = FutureTask.class.getDeclaredField("state");
                        state.setAccessible(true);
                        state.set(task, 2);
                    } catch (NoSuchFieldException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

    }

    public static void main(String[] args) {
        ExecutorService executor = new ThreadPoolExecutor(
                1,
                1,
                1L,
                TimeUnit.MINUTES,
                new LinkedBlockingQueue<>(1),
                new MyRejectedPolicy());
        Future future1 = executor.submit(() -> {
            System.out.println("start task one");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                System.out.println(e.getLocalizedMessage());
            }
        });
        Future future2 = executor.submit(() -> {
            System.out.println("start task two");
        });
        Future future3 = null;
        try {
            /*
             * submit(Runnable task)会将Runnable封装成状态为
             * NEW的FutureTask，DiscardPolicy什么也没做，此时
             * FutureTask状态不变，调用get()时，只有非NEW、
             * 非COMPLETING状态的Future才会返回。
             * */
            future3 = executor.submit(() -> {
                System.out.println("start task three");
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            System.out.println("task one:" + future1.get());
            System.out.println("task two:" + future2.get());
            System.out.println("task three:" + (future3 == null ? null : future3.get()));
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        executor.shutdown();
    }

}
