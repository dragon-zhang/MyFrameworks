package MyTomcat.servlet;

import MyTomcat.http.MyRequest;
import MyTomcat.http.MyResponse;

import java.io.IOException;

/**
 * @author SuccessZhang
 */
public abstract class BaseServlet implements MyServlet {

    @Override
    public void init() throws Exception {

    }

    @Override
    public void service(MyRequest request, MyResponse response) throws Exception {
        if ("GET".equals(request.getMethodType())) {
            this.doGet(request, response);
        } else if ("POST".equals(request.getMethodType())) {
            this.doPost(request, response);
        }
    }

    @Override
    public void destroy() {

    }

    public abstract void doGet(MyRequest request, MyResponse response) throws IOException;

    public abstract void doPost(MyRequest request, MyResponse response) throws IOException;
}
