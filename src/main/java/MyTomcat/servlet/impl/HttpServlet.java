package MyTomcat.servlet.impl;

import MyTomcat.http.MyRequest;
import MyTomcat.http.MyResponse;
import MyTomcat.servlet.BaseServlet;

import java.io.IOException;
import java.io.OutputStream;

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
        OutputStream outputStream = response.getOutputStream();
        outputStream.write(res.getBytes());
        outputStream.close();
    }
}
