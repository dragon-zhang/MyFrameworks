package basic;

/**
 * @author SuccessZhang
 * 创建的线程默认为用户线程，
 * 调用setDaemon(true)才能将其设置为守护线程;
 * <p>
 * 用户线程的生命周期不受父线程的影响,
 * 当用户线程还存在时,JVM进程不会终止;
 * 守护线程的生命周期受父线程的影响,
 * 只要没有用户线程了,即使守护线程还未
 * 运行完毕,JVM进程也会终止.
 */
public class DaemonThreadAndUserThreadDemo {

    public static void main(String[] args) {
        Thread thread = new Thread(() -> {
            for (; ; ) {
            }
        });
        //thread.setDaemon(true);
        thread.start();
        System.out.println("main thread is over");
    }

}
