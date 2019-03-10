package MySpringBoot.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author SuccessZhang
 */
public class StaticServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String requestPath = req.getRequestURI();
        String path = this.getClass().getResource("/").getPath().replace("classes/", "resources") + requestPath;
        resp.setContentType("text/html;charset=UTF-8");
        resp.getWriter().write(getBytes(path));
    }

    private char[] getBytes(String filePath) throws IOException {
        InputStream in = new FileInputStream(filePath);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024 * 4];
        int n;
        while ((n = in.read(buffer)) != -1) {
            out.write(buffer, 0, n);
        }
        char[] data = out.toString().toCharArray();
        in.close();
        return data;
    }
}
