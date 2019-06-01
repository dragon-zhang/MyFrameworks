package DesignPattern.delegate.impl;

import DesignPattern.delegate.Worker;

import java.util.HashMap;
import java.util.Map;

/**
 * @author SuccessZhang
 * 只分派工作，但是自己不干活
 * 要点：委派者持有被委派目标的引用
 * 精简了程序逻辑，提升了代码的可读性
 * 但是违反开闭原则、依赖倒置原则
 */
public class Leader implements Worker {

    private static Map<String, Worker> workers = new HashMap<>();

    static {
        workers.put("架构", new WorkerA());
        workers.put("测试", new WorkerB());
    }

    @Override
    public void doWork(String command) {
        workers.get(command).doWork(command);
    }
}
