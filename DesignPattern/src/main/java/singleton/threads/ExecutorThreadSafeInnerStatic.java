package singleton.threads;

import singleton.LazySingleton;

/**
 * @author SuccessZhang
 */
public class ExecutorThreadSafeInnerStatic implements Runnable {
    @Override
    public void run() {
        LazySingleton singleton = LazySingleton.getInstanceInnerStatic();
        System.out.println(Thread.currentThread().getName() + ":" + singleton);
    }
}
