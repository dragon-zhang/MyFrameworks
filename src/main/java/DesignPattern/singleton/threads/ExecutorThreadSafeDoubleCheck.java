package DesignPattern.singleton.threads;

import DesignPattern.singleton.LazySingleton;

/**
 * @author SuccessZhang
 */
public class ExecutorThreadSafeDoubleCheck implements Runnable {
    @Override
    public void run() {
        LazySingleton singleton = LazySingleton.getInstanceDoubleCheck();
        System.out.println(Thread.currentThread().getName() + ":" + singleton);
    }
}
