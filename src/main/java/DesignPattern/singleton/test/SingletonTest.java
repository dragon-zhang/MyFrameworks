package DesignPattern.singleton.test;

import DesignPattern.singleton.EnumSingleton;
import DesignPattern.singleton.HungrySingleton;
import DesignPattern.singleton.LazySingleton;
import DesignPattern.singleton.threads.*;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.concurrent.*;

/**
 * @author SuccessZhang
 */
public class SingletonTest {

    private static ExecutorService executor = new ThreadPoolExecutor(2,
            4,
            0L,
            TimeUnit.SECONDS,
            //能够接收请求产生的消息队列即可，设置过大会浪费内存
            new LinkedBlockingQueue<>(80),
            new MessageThreadFactory(),
            new MessageRejectedHandler());

    static class MessageThreadFactory implements ThreadFactory {
        @Override
        public Thread newThread(Runnable r) {
            //实际cpu调用的线程
            Thread thread = new Thread(r);
            //设置优先级
            thread.setPriority(Thread.NORM_PRIORITY);
            //创建的线程默认为非守护线程
            return thread;
        }
    }

    static class MessageRejectedHandler implements RejectedExecutionHandler {
        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
            //线程池没有被关闭
            if (executor.isShutdown()) {
                throw new RejectedExecutionException("ThreadPoolExecutor is shutdown");
            } else {
                //任务数量超过最大值（队列长度 + maximumPoolSize）
                throw new RuntimeException("the number of tasks exceeds the maximum");
            }
        }
    }

    private static void enumAntiSerializationTest() {
        try {
            EnumSingleton instance1;
            EnumSingleton instance2 = EnumSingleton.getInstance();
            instance2.setData(new Object());
            FileOutputStream fos = new FileOutputStream("EnumSingleton.obj");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(instance2);
            oos.flush();
            oos.close();
            FileInputStream fis = new FileInputStream("EnumSingleton.obj");
            ObjectInputStream ois = new ObjectInputStream(fis);
            instance1 = (EnumSingleton) ois.readObject();
            ois.close();
            System.out.println(instance1.getData());
            System.out.println(instance2.getData());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void antiSerializationTest(Object instance) {
        try {
            Object instance1;
            FileOutputStream fos = new FileOutputStream("EnumSingleton.obj");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(instance);
            oos.flush();
            oos.close();
            FileInputStream fis = new FileInputStream("EnumSingleton.obj");
            ObjectInputStream ois = new ObjectInputStream(fis);
            instance1 = ois.readObject();
            ois.close();
            System.out.println(instance1);
            System.out.println(instance + "\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        System.out.println("是否线程安全测试---------------------------------------------");
        //饿汉式单例，无法防止序列化
        System.out.println(HungrySingleton.getInstance());
        System.out.println(HungrySingleton.getInstance());
        System.out.println();
        //懒汉式单例，非线程安全，无法防止序列化
        executor.submit(new ExecutorThreadUnsafe());
        executor.submit(new ExecutorThreadUnsafe());
        executor.submit((Runnable) System.out::println);
        //懒汉式单例，线程安全，性能最差，无法防止序列化
        LazySingleton.clear();
        executor.submit(new ExecutorThreadSafe());
        executor.submit(new ExecutorThreadSafe());
        executor.submit((Runnable) System.out::println);
        //懒汉式单例，线程安全，性能稍好，无法防止序列化
        LazySingleton.clear();
        executor.submit(new ExecutorThreadSafeDoubleCheck());
        executor.submit(new ExecutorThreadSafeDoubleCheck());
        executor.submit((Runnable) System.out::println);
        //懒汉式单例，线程安全，性能最好，无法防止序列化
        LazySingleton.clear();
        executor.submit(new ExecutorThreadSafeInnerStatic());
        executor.submit(new ExecutorThreadSafeInnerStatic());
        executor.submit((Runnable) System.out::println);
        //注册式单例，线程安全，性能较好，可防止反射、序列化
        executor.submit(new ExecutorThreadSafeEnum());
        executor.submit(new ExecutorThreadSafeEnum());
        executor.submit((Runnable) System.out::println);
        executor.submit(() -> {
            System.out.println("防序列化测试-------------------------------------------------");
            antiSerializationTest(HungrySingleton.getInstance());
            antiSerializationTest(LazySingleton.getInstanceThreadUnsafe());
            antiSerializationTest(LazySingleton.getInstanceThreadSafe());
            antiSerializationTest(LazySingleton.getInstanceDoubleCheck());
            antiSerializationTest(LazySingleton.getInstanceInnerStatic());
            enumAntiSerializationTest();
        });
    }
}
