package MyTomcat.thread;

import MyTomcat.MyBIOTomcat;
import MyTomcat.http.MyRequest;
import MyTomcat.http.MyResponse;
import MyTomcat.servlet.BaseServlet;
import lombok.AllArgsConstructor;

import java.io.OutputStream;
import java.net.Socket;

/**
 * @author SuccessZhang
 */
@AllArgsConstructor
public class SocketHandler implements Runnable {

    private Socket socket;

    @Override
    public void run() {
        try {
            OutputStream outputStream = socket.getOutputStream();
            MyRequest request = new MyRequest(socket.getInputStream());
            MyResponse response = new MyResponse(outputStream);
            BaseServlet servlet = (BaseServlet) MyBIOTomcat.getServletMapping().get(request.getUrl().replace("/", ""));
            if (servlet == null) {
                outputStream.write(response.response(request.getProtocol(), "没有指定的servlet"));
                outputStream.flush();
                outputStream.close();
                socket.close();
            } else {
                servlet.service(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
