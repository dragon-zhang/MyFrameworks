package ConcurrentProgram.advanced;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author SuccessZhang
 * join()被调用后，该线程会一直阻塞直到子线程运行完毕；
 * CountDownLatch允许在子线程运行的任何时候让await()返回，
 * 不一定非要等到子线程运行结束(灵活)。
 * 当使用线程池时，由于不方便拿到thread的引用，也就是使用join()有些麻烦，
 * 而CountDownLatch可以直接使用(方便)。
 */
public class CountDownLatchDemo {

    public static void main(String[] args) throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(2);
        ExecutorService executor = Executors.newFixedThreadPool(2);
        executor.submit(() -> {
            try {
                Thread.sleep(1000);
                System.out.println("child " + Thread.currentThread().getName() + " is over");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                latch.countDown();
            }
        });
        executor.submit(() -> {
            try {
                Thread.sleep(500);
                System.out.println("child " + Thread.currentThread().getName() + " is over");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                latch.countDown();
            }
        });
        System.out.println("wait child threads over");
        latch.await();
        System.out.println("main thread is over");
        executor.shutdown();
    }

}
