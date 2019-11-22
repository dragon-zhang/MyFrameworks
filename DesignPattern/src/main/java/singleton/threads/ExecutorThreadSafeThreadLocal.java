package singleton.threads;

import singleton.ThreadLocalSingleton;

/**
 * @author SuccessZhang
 */
public class ExecutorThreadSafeThreadLocal implements Runnable {
    @Override
    public void run() {
        ThreadLocalSingleton singleton = ThreadLocalSingleton.getInstance();
        System.out.println(Thread.currentThread().getName() + ":" + singleton);
    }
}
