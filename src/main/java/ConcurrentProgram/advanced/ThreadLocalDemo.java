package ConcurrentProgram.advanced;

/**
 * @author SuccessZhang
 * ThreadLocal线程间单例
 * 实现原理：访问ThreadLocal变量的每一个线程都会有这个
 * 变量的本地副本threadLocals(一个定制化的HashMap)，
 * 后续操作实际操作的是线程本地内存里的变量；
 * <p>
 * 如果当前线程一直不消亡，那么这些本地变量会一直存在，
 * 可能会造成内存溢出，因此在使用完毕后要记得调用ThreadLocal
 * 的remove()方法删除本地变量
 */
public class ThreadLocalDemo {

    private static ThreadLocal<String> threadLocal = new ThreadLocal<>();

    public static void print(String string) {
        System.out.println(string + " before remove:" + threadLocal.get());
        threadLocal.remove();
    }

    public static void main(String[] args) {
        Thread thread1 = new Thread(() -> {
            threadLocal.set("variable1");
            print("thread1");
            System.out.println("thread1 after remove:" + threadLocal.get());
        });
        Thread thread2 = new Thread(() -> {
            threadLocal.set("variable2");
            print("thread2");
            System.out.println("thread2 after remove:" + threadLocal.get());
        });
        thread1.start();
        thread2.start();
    }

}
