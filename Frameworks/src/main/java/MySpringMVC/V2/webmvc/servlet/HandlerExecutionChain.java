package MySpringMVC.V2.webmvc.servlet;

import MySpringMVC.V2.webmvc.servlet.mvc.UrlFileNameViewController;
import lombok.Data;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@Data
public class HandlerExecutionChain {

    private final Object handler;

    private List<HandlerInterceptor> interceptorList = new ArrayList<>();

    public HandlerExecutionChain(Object controller) {
        this.handler = new UrlFileNameViewController(controller);
    }

    public void addInterceptor(HandlerInterceptor interceptor) {
        this.interceptorList.add(interceptor);
    }

    /**
     * Apply postHandle methods of registered interceptors.
     */
    public void applyPostHandle(HttpServletRequest request, HttpServletResponse response, ModelAndView mv) throws Exception {
        if (!interceptorList.isEmpty()) {
            for (int i = interceptorList.size() - 1; i >= 0; i--) {
                HandlerInterceptor interceptor = interceptorList.get(i);
                interceptor.postHandle(request, response, this.handler, mv);
            }
        }
    }

    public boolean applyPreHandle(HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (!interceptorList.isEmpty()) {
            for (int i = interceptorList.size() - 1; i >= 0; i--) {
                HandlerInterceptor interceptor = interceptorList.get(i);
                if (!interceptor.preHandle(request, response, this.handler)) {
                    triggerAfterCompletion(request, response, null);
                    return false;
                }
            }
        }
        return true;
    }

    public void triggerAfterCompletion(HttpServletRequest request, HttpServletResponse response, Exception e) throws Exception {
        if (!interceptorList.isEmpty()) {
            for (int i = interceptorList.size() - 1; i >= 0; i--) {
                HandlerInterceptor interceptor = interceptorList.get(i);
                interceptor.afterCompletion(request, response, this.handler, e);
            }
        }
    }
}
