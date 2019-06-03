package DesignPattern.singleton.threads;

import DesignPattern.singleton.LazySingleton;

/**
 * @author SuccessZhang
 */
public class ExecutorThreadUnsafe implements Runnable {
    @Override
    public void run() {
        LazySingleton singleton = LazySingleton.getInstanceThreadUnsafe();
        System.out.println(Thread.currentThread().getName() + ":" + singleton);
    }
}
