package ConcurrentProgram;

/**
 * @author SuccessZhang
 */
public class AlternateDemo {

    static class MyThread implements Runnable {

        private static int i = 1;

        @Override
        public synchronized void run() {
            for (; i <= 52; i++) {
                System.out.print(i);
                if (i % 2 == 0) {
                    System.out.print((char) ('A' + (i - 1) / 2) + " ");
                }
            }
        }

    }

    public static void main(String[] args) {
        Runnable runnable = new MyThread();
        Thread thread1 = new Thread(runnable);
        Thread thread2 = new Thread(runnable);
        thread1.start();
        thread2.start();
    }
}