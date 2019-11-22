package singleton.threads;

import singleton.EnumSingleton;

/**
 * @author SuccessZhang
 */
public class ExecutorThreadSafeEnum implements Runnable {
    @Override
    public void run() {
        EnumSingleton singleton = EnumSingleton.getInstance();
        System.out.println(Thread.currentThread().getName() + ":" + singleton);
    }
}
