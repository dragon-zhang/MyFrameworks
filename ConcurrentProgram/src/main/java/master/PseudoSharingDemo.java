package master;

/**
 * @author SuccessZhang
 * 当CPU访问某个变量时，首先会去看CPU Cache内是否有该变量，如果没有就去
 * 主内存获取该变量，然后把该变量所在内存区域的一个缓存行大小的内存复制
 * 到CPU Cache，由于存放到缓存行的是内存块而不是单个变量，可能把多个变量
 * 放在同一个缓存行中。
 * 当多个线程同时修改一个缓存行里面的多个变量时，由于同时只能有一个线程
 * 操作缓存行，相比每个变量一个缓存行，其性能有所下降，这就是伪共享。
 * <p>
 * JDK8中提供了{@link sun.misc.Contended}注解来解决伪共享问题。
 * 用户路径下的类需要使用这个注解，需要添加JVM参数：
 * -XX:-RestrictContended 填充默认宽度为128
 * -XX:-ContendedPaddingWidth 宽度 自定义填充宽度
 */
public class PseudoSharingDemo {

    public static void main(String[] args) {
        long[][] array1 = new long[2048][2048];
        long startTime1 = System.currentTimeMillis();
        for (int i = 0; i < 2048; i++) {
            for (int j = 0; j < 2048; j++) {
                array1[i][j] = i * 2 + j;
            }
        }
        long endTime1 = System.currentTimeMillis();
        System.out.println("cache time:" + (endTime1 - startTime1));

        long[][] array2 = new long[2048][2048];
        long startTime2 = System.currentTimeMillis();
        for (int i = 0; i < 2048; i++) {
            for (int j = 0; j < 2048; j++) {
                array2[j][i] = i * 2 + j;
            }
        }
        long endTime2 = System.currentTimeMillis();
        System.out.println("no cache time:" + (endTime2 - startTime2));
    }

}
