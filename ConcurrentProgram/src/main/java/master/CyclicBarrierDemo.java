package master;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author SuccessZhang
 * {@link java.util.concurrent.CountDownLatch}是一次性的，
 * {@link CyclicBarrier}具有可复用性。
 */
public class CyclicBarrierDemo {

    public static void main(String[] args) {
        CyclicBarrier barrier = new CyclicBarrier(2, () -> {
            //当所有子线程全部到达屏障后，将会执行这段代码
            System.out.println("all threads has finished this step");
        });
        ExecutorService executor = Executors.newFixedThreadPool(2);
        executor.submit(() -> {
            try {
                System.out.println(Thread.currentThread().getName() + " finished step1");
                barrier.await();
                System.out.println(Thread.currentThread().getName() + " finished step2");
                barrier.await();
                System.out.println(Thread.currentThread().getName() + " finished step3");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            }
        });
        executor.submit(() -> {
            try {
                System.out.println(Thread.currentThread().getName() + " finished step1");
                barrier.await();
                System.out.println(Thread.currentThread().getName() + " finished step2");
                barrier.await();
                System.out.println(Thread.currentThread().getName() + " finished step3");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            }
        });
        executor.shutdown();
    }

}
