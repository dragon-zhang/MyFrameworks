package MyTomcat.servlet.impl;

import MyTomcat.http.MyRequest;
import MyTomcat.http.MyResponse;
import MyTomcat.servlet.BaseServlet;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * @author SuccessZhang
 */
public class HttpServlet extends BaseServlet {
    @Override
    public void doGet(MyRequest request, MyResponse response) throws IOException {
        this.doPost(request, response);
    }

    @Override
    public void doPost(MyRequest request, MyResponse response) throws IOException {
        String res = "test";
        System.out.println("MyTomcat returns : " + res);
        SocketChannel channel = response.getChannel();
        if (channel != null) {
            channel.write(ByteBuffer.wrap(res.getBytes()));
            channel.close();
        } else {
            OutputStream outputStream = response.getOutputStream();
            outputStream.write(res.getBytes());
            outputStream.close();
        }
    }
}
