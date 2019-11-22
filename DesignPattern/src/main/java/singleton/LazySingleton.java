package singleton;

import java.io.Serializable;

/**
 * @author SuccessZhang
 * 懒汉式单例
 */
public class LazySingleton implements Serializable {
    private static LazySingleton INSTANCE;

    private LazySingleton() {
        //可以加上以下代码，防止反射调用方法
        /*if (Holder.INSTANCE != null) {
            throw new RuntimeException("不允许创建多个实例");
        }*/
    }

    /**
     * 此写法线程不安全
     */
    public static final LazySingleton getInstanceThreadUnsafe() {
        if (INSTANCE == null) {
            INSTANCE = new LazySingleton();
        }
        return INSTANCE;
    }

    /**
     * 此写法线程安全，但是性能极差
     */
    public synchronized final static LazySingleton getInstanceThreadSafe() {
        if (INSTANCE == null) {
            INSTANCE = new LazySingleton();
        }
        return INSTANCE;
    }

    /**
     * 此写法线程安全，性能相对整个方法加上synchronized好上不少
     */
    public final static LazySingleton getInstanceDoubleCheck() {
        if (INSTANCE == null) {
            synchronized (LazySingleton.class) {
                if (INSTANCE == null) {
                    INSTANCE = new LazySingleton();
                }
            }
        }
        return INSTANCE;
    }

    /**
     * 此写法线程安全，也兼顾synchronized性能问题
     * 每一个关键字都不是多余的
     * static 是为了使单例的空间共享
     * final 保证这个方法不会被重写，重载
     */
    public static final LazySingleton getInstanceInnerStatic() {
        //在返回结果以前，一定会先加载内部类
        return Holder.INSTANCE;
    }

    public static final void clear() {
        INSTANCE = null;
    }

    /*
     * 增加readResolve()方法返回实例，解决了序列化破坏单例的问题。
     * 进入ObjectInputStream的readObject()，查看源码，
     * 追踪至readOrdinaryObject(boolean unshared)2051-2053行和2074-2017行可以发现，
     * 实际上单例仍然被实例化了两次，只是新创建的对象没有被返回就被GC回收了。
     */
    /*private Object readResolve() {
        return INSTANCE == null ? Holder.INSTANCE : INSTANCE;
    }*/

    /**
     * 默认不加载
     */
    private static class Holder {
        private static final LazySingleton INSTANCE = new LazySingleton();
    }
}
