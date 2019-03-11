package core.niostudy;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.concurrent.*;

/**
 * @author SuccessZhang
 */

public enum NIOHandler implements ThreadFactory, RejectedExecutionHandler {

    //枚举单例
    INSTANCE;

    private static ExecutorService bossPool = new ThreadPoolExecutor(6,
            15,
            30000L,
            TimeUnit.SECONDS,
            //能够接收请求产生的消息队列即可，设置过大会浪费内存
            new LinkedBlockingQueue<>(800),
            INSTANCE, INSTANCE);

    private static ExecutorService workerPool = new ThreadPoolExecutor(6,
            15,
            30000L,
            TimeUnit.SECONDS,
            //能够接收请求产生的消息队列即可，设置过大会浪费内存
            new LinkedBlockingQueue<>(800),
            INSTANCE, INSTANCE);

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

    public static void read(final SelectionKey key) {
        //获得线程并执行
        bossPool.submit(() -> {
            try {
                SocketChannel readChannel = (SocketChannel) key.channel();
                // I/O读数据操作
                ByteBuffer buffer = ByteBuffer.allocate(1024);
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                int len;
                while (true) {
                    buffer.clear();
                    len = readChannel.read(buffer);
                    if (len == -1) {
                        break;
                    }
                    buffer.flip();
                    while (buffer.hasRemaining()) {
                        outputStream.write(buffer.get());
                    }
                }
                System.out.println("服务器端接收到的数据：" + new String(outputStream.toByteArray()));
                //将数据添加到key中
                key.attach(outputStream);
                //将注册写操作添加到队列中
                NIOServerSocket.addWriteQueue(key);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public static void write(final SelectionKey key) {
        //拿到线程并执行
        workerPool.submit(() -> {
            try {
                // 写操作
                SocketChannel writeChannel = (SocketChannel) key.channel();
                //拿到客户端传递的数据
                ByteArrayOutputStream attachment = (ByteArrayOutputStream) key.attachment();
                System.out.println("客户端发送的数据：" + new String(attachment.toByteArray()));
                ByteBuffer buffer = ByteBuffer.allocate(1024);
                String message = "你好,我是服务器!";
                buffer.put(message.getBytes());
                buffer.flip();
                writeChannel.write(buffer);
                writeChannel.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}