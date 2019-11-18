package ConcurrentProgram.master;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * @author SuccessZhang
 * {@link java.util.concurrent.Semaphore}不需要知道需要同步的线程个数，
 * 而是在需要同步的地方调用acquire(int permits)指定需要同步的线程个数。
 * <p>
 * 以下demo是用Semaphore来实现类似CyclicBarrier的可重用功能，可以发现
 * Semaphore比CyclicBarrier更加灵活，但是定制化的功能需要自己实现。
 */
public class SemaphoreDemo {

    private static Semaphore semaphore = new Semaphore(0);

    public static void main(String[] args) throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(2);
        executor.submit(() -> {
            System.out.println(Thread.currentThread().getName() + " finished step1");
            semaphore.release();
        });
        executor.submit(() -> {
            System.out.println(Thread.currentThread().getName() + " finished step1");
            semaphore.release();
        });
        semaphore.acquire(2);
        System.out.println("all threads has finished this step");
        executor.submit(() -> {
            System.out.println(Thread.currentThread().getName() + " finished step2");
            semaphore.release();
        });
        executor.submit(() -> {
            System.out.println(Thread.currentThread().getName() + " finished step2");
            semaphore.release();
        });
        executor.submit(() -> {
            System.out.println(Thread.currentThread().getName() + " finished step2");
            semaphore.release();
        });
        semaphore.acquire(3);
        System.out.println("all threads has finished this step");
        executor.submit(() -> {
            System.out.println(Thread.currentThread().getName() + " finished step3");
            semaphore.release();
        });
        executor.submit(() -> {
            System.out.println(Thread.currentThread().getName() + " finished step3");
            semaphore.release();
        });
        semaphore.acquire(2);
        executor.shutdown();
    }

}
