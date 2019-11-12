package ConcurrentProgram.master;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * @author SuccessZhang
 * 对于{@link java.util.concurrent.locks.ReentrantLock}来说，
 * state表示当前线程获取锁的可重入次数；
 * <p>
 * 对于{@link java.util.concurrent.locks.ReentrantReadWriteLock}来说，
 * state低16位表示获取到写锁线程的可重入次数，
 * 高16位表示获取该读锁的次数；
 * <p>
 * 对于{@link java.util.concurrent.Semaphore}来说，
 * state表示当前可用信号的个数；
 * <p>
 * 对于{@link java.util.concurrent.CountDownLatch}来说，
 * state表示计数器当前值；
 */
public class NonReentrantLockDemo {

    /**
     * 自定义不可重入锁。
     * state=0时表示目前锁没有被线程持有；
     * state=1时表示锁已经被某一个线程持有。
     */
    public static class NonReentrantLock implements Lock, java.io.Serializable {

        private final Sync sync = new Sync();

        private static class Sync extends AbstractQueuedSynchronizer {

            /**
             * 尝试获得锁。
             */
            @Override
            public boolean tryAcquire(int acquires) {
                if (acquires == 1 && compareAndSetState(0, 1)) {
                    setExclusiveOwnerThread(Thread.currentThread());
                    return true;
                }
                return false;
            }

            /**
             * 尝试释放锁。
             */
            @Override
            public boolean tryRelease(int acquires) {
                if (acquires == 1) {
                    if (getState() == 0) {
                        throw new IllegalMonitorStateException();
                    }
                    setExclusiveOwnerThread(null);
                    setState(0);
                    return true;
                }
                return false;
            }

            final AbstractQueuedSynchronizer.ConditionObject newCondition() {
                return new ConditionObject();
            }

            /**
             * 锁是否被持有。
             */
            @Override
            protected boolean isHeldExclusively() {
                return getState() == 1;
            }

        }

        @Override
        public void lock() {
            sync.acquire(1);
        }

        @Override
        public void lockInterruptibly() throws InterruptedException {
            sync.acquireInterruptibly(1);
        }

        @Override
        public boolean tryLock() {
            return sync.tryAcquire(1);
        }

        @Override
        public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
            return sync.tryAcquireNanos(1, unit.toNanos(time));
        }

        @Override
        public void unlock() {
            sync.release(1);
        }

        @Override
        public Condition newCondition() {
            return sync.newCondition();
        }
    }

    public static void main(String[] args) {
        int i = 0;
        int maxSize = 10;
        Queue<String> queue = new LinkedBlockingQueue<>();
        NonReentrantLock lock = new NonReentrantLock();
        Condition notFull = lock.newCondition();
        Condition notEmpty = lock.newCondition();

        Thread producer = new Thread(() -> {
            try {
                lock.lock();
                Thread.sleep(1000);
                while (queue.size() == maxSize) {
                    notEmpty.await();
                }
                String string = String.valueOf(i);
                System.out.println("offered " + string);
                queue.offer(string);
                notFull.signalAll();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        });
        Thread consumer = new Thread(() -> {
            try {
                lock.lock();
                Thread.sleep(1000);
                while (queue.size() == 0) {
                    notFull.await();
                }
                System.out.println("consumed " + queue.poll());
                notEmpty.signalAll();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        });
        producer.start();
        consumer.start();
    }

}
