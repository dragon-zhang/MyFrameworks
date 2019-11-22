package delegate.impl;

import delegate.Worker;

/**
 * @author SuccessZhang
 */
public class WorkerB implements Worker {
    @Override
    public void doWork(String command) {
        System.out.println("我是员工B，擅长测试，开始完成" + command + "工作");
    }
}
