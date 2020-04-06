package MySpringMVC.V2.webmvc.servlet.mvc;

import MySpringMVC.V2.annotation.RequestParam;
import MySpringMVC.V2.web.UrlPathHelper;
import MySpringMVC.V2.webmvc.servlet.HandlerAdapter;
import MySpringMVC.V2.webmvc.servlet.HandlerMapping;
import MySpringMVC.V2.webmvc.servlet.ModelAndView;
import com.alibaba.fastjson.JSON;
import lombok.Data;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class SimpleControllerHandlerAdapter implements HandlerAdapter {

    private List<HandlerMapping> handlerMappings;

    public SimpleControllerHandlerAdapter(List<HandlerMapping> handlerMappings) {
        this.handlerMappings = handlerMappings;
    }

    /**
     * Given a handler instance, return whether or not this {@code HandlerAdapter}
     * can support it. Typical HandlerAdapters will base the decision on the handler
     * type. HandlerAdapters will usually only support one handler type each.
     * <p>A typical implementation:
     * <p>{@code
     * return (handler instanceof MyHandler);
     * }
     *
     * @param handler handler object to check
     * @return whether or not this object can use the given handler
     */
    @Override
    public boolean supports(Object handler) {
        return (handler instanceof Controller);
    }

    /**
     * Use the given handler to handle this request.
     * The workflow that is required may vary widely.
     *
     * @param request  current HTTP request
     * @param response current HTTP response
     * @param handler  handler to use. This object must have previously been passed
     *                 to the {@code supports} method of this interface, which must have
     *                 returned {@code true}.
     * @return ModelAndView object with the name of the view and the required
     * model data, or {@code null} if the request has been handled directly
     * @throws Exception in case of errors
     */
    @Override
    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String url = UrlPathHelper.getRequestUrl(request);
        for (HandlerMapping handlerMapping : this.handlerMappings) {
            if (!handlerMapping.getPattern().matcher(url).matches()) {
                continue;
            }
            String httpServletRequestClassName = HttpServletRequest.class.getName();
            String httpServletResponseClassName = HttpServletResponse.class.getName();
            Map<String, Integer> paramIndexMapping = new HashMap<>(4);
            Method method = handlerMapping.getMethod();
            Parameter[] parameters = method.getParameters();
            for (int i = 0; i < parameters.length; i++) {
                Class<?> parameterType = parameters[i].getType();
                if (parameterType.equals(HttpServletRequest.class)) {
                    paramIndexMapping.put(httpServletRequestClassName, i);
                    continue;
                }
                if (parameterType.equals(HttpServletResponse.class)) {
                    paramIndexMapping.put(httpServletResponseClassName, i);
                    continue;
                }
                Annotation[] parameterAnnotation = parameters[i].getDeclaredAnnotations();
                for (Annotation annotation : parameterAnnotation) {
                    if (annotation instanceof RequestParam) {
                        String paramName = ((RequestParam) annotation).value();
                        if ("".equals(paramName.trim())) {
                            paramName = parameters[i].getName();
                        }
                        paramIndexMapping.put(paramName, i);
                    }
                }
            }
            Map<String, String[]> paramMap = request.getParameterMap();
            Object[] paramValues = new Object[parameters.length];
            for (Map.Entry<String, String[]> param : paramMap.entrySet()) {
                String key = param.getKey();
                if (!paramIndexMapping.containsKey(key)) {
                    continue;
                }
                String[] value = param.getValue();
                String paramValue = null;
                if (value.length == 1) {
                    paramValue = JSON.toJSONString(value[0]);
                } else if (value.length > 1) {
                    paramValue = JSON.toJSONString(value);
                }
                Integer index = paramIndexMapping.get(key);
                paramValues[index] = JSON.parseObject(paramValue, parameters[index].getType());
            }
            Integer index = paramIndexMapping.get(httpServletRequestClassName);
            if (index != null) {
                paramValues[index] = request;
            }
            index = paramIndexMapping.get(httpServletResponseClassName);
            if (index != null) {
                paramValues[index] = response;
            }
            Controller controller = (Controller) handler;
            return controller.handleRequest(method, paramValues);
        }
        return null;
    }
}
