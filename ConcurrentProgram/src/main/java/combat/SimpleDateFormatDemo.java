package combat;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * @author SuccessZhang
 * {@link SimpleDateFormat}不是线程安全的，
 * 其根本原因是成员字段{@link java.util.Calendar}非线程安全。
 * 这里使用ThreadLocal来解决。
 */
public class SimpleDateFormatDemo {

    public static void main(String[] args) {
        NonThreadSafeDemo();
        System.out.println("-------------------------------------------------");
        ThreadLocalDemo();
    }

    private static void NonThreadSafeDemo() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                try {
                    System.out.println(sdf.parse("2017-12-13 15:17:27"));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }

    private static void ThreadLocalDemo() {
        ThreadLocal<DateFormat> threadLocal = ThreadLocal.withInitial(() -> {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        });
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                try {
                    System.out.println(threadLocal.get().parse("2017-12-13 15:17:27"));
                } catch (ParseException e) {
                    e.printStackTrace();
                } finally {
                    threadLocal.remove();
                }
            }).start();
        }
    }

}
