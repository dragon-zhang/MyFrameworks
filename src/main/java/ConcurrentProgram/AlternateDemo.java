package ConcurrentProgram;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author SuccessZhang
 */
public class AlternateDemo {

    public static class PrintChar extends Thread {

        private char[] chars;
        private Condition cCondition, nCondition;
        private Lock lock;

        public PrintChar(char[] chars, Lock lock, Condition cCondition, Condition nCondition) {
            this.chars = chars;
            this.nCondition = nCondition;
            this.cCondition = cCondition;
            this.lock = lock;
        }

        @Override
        public void run() {
            for (char c : chars) {
                System.out.println(c);
                lock.lock();
                try {
                    cCondition.signal();
                    nCondition.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }
            }
            try {
                lock.lock();
                nCondition.signal();
            } finally {
                lock.unlock();
            }
        }
    }

    public static class PrintNum extends Thread {

        private int[] numbers;
        private Condition cCondition, nCondition;
        private Lock lock;

        public PrintNum(int[] numbers, Lock lock, Condition cCondition, Condition nCondition) {
            this.numbers = numbers;
            this.nCondition = nCondition;
            this.cCondition = cCondition;
            this.lock = lock;
        }

        @Override
        public void run() {
            for (int i = 0; i < numbers.length; i += 2) {
                System.out.print(numbers[i] + "" + numbers[i + 1]);
                try {
                    lock.lock();
                    nCondition.signal();
                    cCondition.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }
            }
            try {
                lock.lock();
                nCondition.signal();
            } finally {
                lock.unlock();
            }
        }
    }

    public static void main(String[] args) {
        Lock lock = new ReentrantLock();
        Condition cCondition = lock.newCondition();
        Condition nCondition = lock.newCondition();
        char[] c = new char[26];
        int[] num = new int[52];
        for (int i = 0; i < 52; i++) {
            num[i] = i + 1;
        }
        for (int i = 0; i < 26; i++) {
            c[i] = (char) (i + 65);
        }
        PrintNum printNum = new PrintNum(num, lock, cCondition, nCondition);
        PrintChar printChar = new PrintChar(c, lock, cCondition, nCondition);
        printNum.start();
        printChar.start();
    }
}