package MyRPC.server;

import MyRPC.server.impl.TestServiceImpl;
import MyRPC.server.thread.ServerHandler;
import com.example.disperse.service.TestService;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;

/**
 * @author SuccessZhang
 */

public enum ServerPublisher implements ThreadFactory, RejectedExecutionHandler {

    //枚举单例
    INSTANCE;

    private ExecutorService executorService = new ThreadPoolExecutor(6,
            15,
            30000L,
            TimeUnit.SECONDS,
            //能够接收请求产生的消息队列即可，设置过大会浪费内存
            new LinkedBlockingQueue<>(800),
            this, this);

    public void publish(Object server, int port) throws IOException {
        //启动一个服务
        ServerSocket serverSocket = new ServerSocket(port);
        while (true) {
            Socket socket = serverSocket.accept();
            executorService.execute(new ServerHandler(server, socket));
        }
    }

    @Override
    public Thread newThread(Runnable r) {
        //实际cpu调用的线程
        Thread thread = new Thread(r);
        //设置优先级
        thread.setPriority(Thread.NORM_PRIORITY);
        //创建的线程默认为非守护线程，这里设置成守护线程
        thread.setDaemon(true);
        return thread;
    }

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

    public static void main(String[] args) throws IOException {
        TestService testService = new TestServiceImpl();
        System.out.println(testService.hello("server"));
        ServerPublisher.INSTANCE.publish(testService, 9999);
    }
}
