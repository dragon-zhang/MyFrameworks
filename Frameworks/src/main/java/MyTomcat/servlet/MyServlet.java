package MyTomcat.servlet;

import MyTomcat.http.MyRequest;
import MyTomcat.http.MyResponse;

/**
 * @author SuccessZhang
 */
public interface MyServlet {

    void init() throws Exception;

    void service(MyRequest request, MyResponse response) throws Exception;

    void destroy();
}
