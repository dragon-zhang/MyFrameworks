package ConcurrentProgram.advanced;

/**
 * @author SuccessZhang
 * ThreadLocal不支持继承特性，比如说在父线程中set了
 * 一个值，在子线程中无法获取，按照ThreadLocal的特性，
 * 这是正常现象，但是有没有办法能让子线程获取到父线程
 * set的值？答案是使用InheritableThreadLocal。
 * <p>
 * 实现原理：实现逻辑和ThreadLocal类似，只是操作的对象
 * 从threadLocals变成inheritableThreadLocals。
 */
public class InheritableThreadLocalDemo {

    public static void main(String[] args) {
        ThreadLocal<String> threadLocal = new ThreadLocal<>();
        InheritableThreadLocal<String> inheritableThreadLocal = new InheritableThreadLocal<>();
        threadLocal.set("hello");
        inheritableThreadLocal.set("hello");
        Thread thread = new Thread(() -> {
            System.out.println("ThreadLocal thread : " + threadLocal.get());
            System.out.println("InheritableThreadLocal thread : " + inheritableThreadLocal.get());
        });
        thread.start();
        System.out.println("ThreadLocal main : " + threadLocal.get());
        System.out.println("InheritableThreadLocal main : " + inheritableThreadLocal.get());
    }

}
