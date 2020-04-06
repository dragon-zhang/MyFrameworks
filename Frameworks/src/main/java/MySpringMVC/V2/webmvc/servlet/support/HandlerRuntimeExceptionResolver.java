package MySpringMVC.V2.webmvc.servlet.support;

import MySpringMVC.V2.webmvc.servlet.HandlerExceptionResolver;
import MySpringMVC.V2.webmvc.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

public class HandlerRuntimeExceptionResolver extends HandlerExceptionResolver {

    @Override
    public boolean support(Exception exception) {
        return exception.getCause() instanceof RuntimeException;
    }

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        ex.printStackTrace();
        Throwable cause = ex.getCause();
        Map<String, Object> model = new HashMap<>(2);
        model.put("detail", "这是运行时的异常，" + cause.getMessage());
        StringBuilder sb = new StringBuilder("\n");
        for (StackTraceElement stackTraceElement : cause.getStackTrace()) {
            sb.append(stackTraceElement).append("\n");
        }
        model.put("stackTrace", sb.toString());
        return new ModelAndView("500", model);
    }
}
