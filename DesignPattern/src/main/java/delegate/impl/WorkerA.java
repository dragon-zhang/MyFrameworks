package delegate.impl;

import delegate.Worker;

/**
 * @author SuccessZhang
 */
public class WorkerA implements Worker {
    @Override
    public void doWork(String command) {
        System.out.println("我是员工A，擅长架构，开始完成" + command + "工作");
    }
}
