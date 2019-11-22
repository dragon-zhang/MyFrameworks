package singleton;

/**
 * @author SuccessZhang
 */
public enum EnumSingleton {
    //枚举单例
    INSTANCE;
    /**
     * 用于存取数据
     */
    private Object data;

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public static EnumSingleton getInstance() {
        return INSTANCE;
    }
}