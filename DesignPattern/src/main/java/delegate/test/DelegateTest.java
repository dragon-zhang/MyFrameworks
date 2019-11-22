package delegate.test;

import delegate.Boss;
import delegate.impl.Leader;

/**
 * @author SuccessZhang
 * 业务场景：不同的boss给同一个leader下达不同的指令，
 * 需要leader根据下面员工各自擅长的部分来分派工作
 */
public class DelegateTest {
    public static void main(String[] args) {
        Leader leader = new Leader();
        new Boss().giveCommand("架构", leader);
        new Boss().giveCommand("测试", leader);
    }
}
