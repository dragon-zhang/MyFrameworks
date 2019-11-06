package ConcurrentProgram.advanced;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author SuccessZhang
 * 独占锁：锁只能被单个线程持有；
 * 共享锁：锁可以被多个线程共同持有。
 * <p>
 * 独占锁/共享锁的典型实现就是{@link java.util.concurrent.locks.ReadWriteLock}
 * 读写锁，同时有A、B线程访问
 * A读B读不互斥
 * A读B写互斥，阻塞A
 * A写B写互斥，阻塞后访问的线程
 */
public class ExclusiveLockAndSharedLockDemo {

    private Map<String, Object> map = new HashMap<>();
    private ReadWriteLock lock = new ReentrantReadWriteLock();
    private Lock readLock = lock.readLock();
    private Lock writeLock = lock.writeLock();

    public Object getData(String key) {
        readLock.lock();
        try {
            return map.get(key);
        } finally {
            readLock.unlock();
        }
    }

    public void putData(String key, Object data) {
        writeLock.lock();
        try {
            map.put(key, data);
        } finally {
            writeLock.unlock();
        }
    }

    public static void main(String[] args) {
        ExclusiveLockAndSharedLockDemo demo = new ExclusiveLockAndSharedLockDemo();
        new Thread(() -> demo.putData("1", 1)).start();
        new Thread(() -> System.out.println(demo.getData("1"))).start();
    }
}
