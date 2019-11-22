package singleton;

import java.io.Serializable;

/**
 * @author SuccessZhang
 * 饿汉式单例
 */
public class HungrySingleton implements Serializable {
    private static final HungrySingleton INSTANCE = new HungrySingleton();

    /*private static final HungrySingleton INSTANCE;
    static {
        INSTANCE=new HungrySingleton();
    }*/

    private HungrySingleton() {
    }

    public static HungrySingleton getInstance() {
        return INSTANCE;
    }
}
