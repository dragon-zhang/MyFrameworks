package delegate;

import delegate.impl.Leader;

/**
 * @author SuccessZhang
 */
public class Boss {
    public void giveCommand(String command, Leader leader) {
        leader.doWork(command);
    }
}
