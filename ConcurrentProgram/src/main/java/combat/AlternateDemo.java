package combat;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author SuccessZhang
 * 多线程交替打印ABC的例子。
 */
public class AlternateDemo {

    private static int count = 0;

    private static class Printer extends Thread {

        private Lock lock;

        private int id;

        private Condition[] conditions;

        private String content;

        public Printer(Lock lock, int id, Condition[] conditions, String content) {
            this.lock = lock;
            this.id = id;
            this.conditions = conditions;
            this.content = content;
        }

        @Override
        public void run() {
            try {
                lock.lock();
                for (int i = 0; i < 10; i++) {
                    //注意这里是不等于0，也就是说在count % 3为0之前，当前线程一直阻塞状态
                    while (count % conditions.length != id) {
                        conditions[id].await(); // A释放lock锁
                    }
                    System.out.print(" " + Thread.currentThread().getName() + " " + content);
                    if (count % conditions.length == conditions.length - 1) {
                        System.out.println();
                    }
                    count++;
                    conditions[(id + 1) % conditions.length].signal(); // A执行完唤醒B线程
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }
    }

    public static void main(String[] args) {
        Lock lock = new ReentrantLock();
        Condition[] conditions = new Condition[3];
        for (int i = 0; i < conditions.length; i++) {
            conditions[i] = lock.newCondition();
        }
        for (int i = 0; i < conditions.length; i++) {
            new Printer(lock, i, conditions, String.valueOf((char) ('A' + i))).start();
        }
    }
}