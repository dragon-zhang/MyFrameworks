package MyTomcat.thread;

import MyTomcat.MyTomcat;
import MyTomcat.http.MyRequest;
import MyTomcat.http.MyResponse;
import MyTomcat.servlet.impl.HttpServlet;
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
            MyRequest request = new MyRequest(socket.getInputStream());
            MyResponse response = new MyResponse(socket.getOutputStream());
            HttpServlet httpServlet = (HttpServlet) MyTomcat.getServletMapping().get(request.getUrl().replace("/", ""));
            if (httpServlet == null) {
                OutputStream outputStream = socket.getOutputStream();
                outputStream.write(response.response(request.getProtocol(), "没有指定的servlet"));
                outputStream.flush();
                outputStream.close();
                socket.close();
            } else {
                httpServlet.service(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
