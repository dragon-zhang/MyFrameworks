package core.niostudy;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * @author SuccessZhang
 */
public class NIOClientSocket {

    public static void main(String[] args) throws IOException {
        long start = System.currentTimeMillis();
        //使用线程模拟用户 并发访问
        for (int i = 0; i < 500; i++) {
            int finalI = i;
            new Thread(() -> {
                try {
                    //1.创建SocketChannel
                    SocketChannel socketChannel = SocketChannel.open();
                    //2.连接服务器
                    socketChannel.connect(new InetSocketAddress("localhost", 60000));
                    //写数据
                    String msg = "GET /MyServlet HTTP/1.1 " + finalI;
                    ByteBuffer buffer = ByteBuffer.allocate(1024);
                    buffer.put(msg.getBytes());
                    buffer.flip();
                    socketChannel.write(buffer);
                    socketChannel.shutdownOutput();
                    //读数据
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    int len;
                    while (true) {
                        buffer.clear();
                        len = socketChannel.read(buffer);
                        if (len == -1) {
                            break;
                        }
                        buffer.flip();
                        while (buffer.hasRemaining()) {
                            bos.write(buffer.get());
                        }
                    }
                    System.out.println("客户端收到:" + new String(bos.toByteArray()));
                    socketChannel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
        }
        long end = System.currentTimeMillis();
        System.out.println("--------------------------------------------");
        System.out.println(end - start);
    }
}