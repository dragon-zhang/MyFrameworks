package ConcurrentProgram.advanced;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

/**
 * @author SuccessZhang
 * java中的CAS操作核心实现源于{@link sun.misc.Unsafe}类
 */
public class UnsafeDemo {

    private volatile long state = 0;

    public static void main(String[] args) {
        try {
            /*
             * 这里如果通过Unsafe.getUnsafe()获取Unsafe会抛出SecurityException
             * 因为Unsafe是rt.jar中的核心类，只能通过Bootstrap加载，
             * 而main函数所在的类是使用AppClassLoader加载的
             */
            Field field = Unsafe.class.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            Unsafe unsafe = (Unsafe) field.get(null);
            long stateOffset = unsafe.objectFieldOffset(
                    UnsafeDemo.class.getDeclaredField("state"));
            UnsafeDemo demo = new UnsafeDemo();
            System.out.println(unsafe.compareAndSwapInt(demo, stateOffset, 0, 1));
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

}
