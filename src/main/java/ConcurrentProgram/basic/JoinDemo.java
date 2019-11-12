package ConcurrentProgram.basic;

/**
 * @author SuccessZhang
 * 在B线程中调用A.join()能够阻塞B线程，
 * 使B线程等到A线程执行完毕再开始执行。
 */
public class JoinDemo {

    private static volatile int i = 0;

    public static void main(String[] args) {
        Thread thread1 = new Thread(() -> {
            while (i < 3) {
                System.out.println(Thread.currentThread().getName() + " " + (i++));
            }
        });
        Thread thread2 = new Thread(() -> {
            while (i < 7) {
                System.out.println(Thread.currentThread().getName() + " " + (i++));
            }
        });
        Thread thread3 = new Thread(() -> {
            try {
                thread1.join();
                thread2.join();
                while (i < 10) {
                    System.out.println(Thread.currentThread().getName() + " " + (i++));
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        thread1.start();
        thread2.start();
        thread3.start();
    }
}
