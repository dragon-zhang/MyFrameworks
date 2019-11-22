package singleton.threads;

import singleton.LazySingleton;

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
