package DesignPattern.singleton.threads;

import DesignPattern.singleton.LazySingleton;

/**
 * @author SuccessZhang
 */
public class ExecutorThreadSafe implements Runnable {
    @Override
    public void run() {
        LazySingleton singleton = LazySingleton.getInstanceThreadSafe();
        System.out.println(Thread.currentThread().getName() + ":" + singleton);
    }
}
