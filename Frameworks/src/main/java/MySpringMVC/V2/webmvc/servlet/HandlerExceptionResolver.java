package MySpringMVC.V2.webmvc.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

public class HandlerExceptionResolver {

    public boolean support(Exception exception) {
        return true;
    }

    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        ex.printStackTrace();
        Throwable cause = ex.getCause();
        Map<String, Object> model = new HashMap<>(2);
        model.put("detail", cause.getMessage());
        StringBuilder sb = new StringBuilder("\n");
        for (StackTraceElement stackTraceElement : cause.getStackTrace()) {
            sb.append(stackTraceElement).append("\n");
        }
        model.put("stackTrace", sb.toString());
        return new ModelAndView("500", model);
    }
}
