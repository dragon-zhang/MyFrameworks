package ConcurrentProgram.combat;

/**
 * @author SuccessZhang
 * @date 2019/11/21
 * 创建有名字的线程，方便日后定位问题。
 */
public class NamedThreadDemo {

    public static void main(String[] args) {
        Thread thread1 = new Thread(() -> {
            throw new RuntimeException("just for test");
        }, "name");
        thread1.start();
    }

}
