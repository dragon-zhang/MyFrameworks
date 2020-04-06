package MySpringMVC.V2.webmvc.servlet;

import MySpringMVC.V2.context.ConfigurableApplicationContext;
import MySpringMVC.V2.web.UrlPathHelper;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Comparator;
import java.util.regex.Pattern;

@Data
@RequiredArgsConstructor
public class HandlerMapping implements Comparator<HandlerMapping> {

    private final ConfigurableApplicationContext context;

    private final Pattern pattern;

    private final Object handler;

    private final Method method;

    /**
     * Return a handler and any interceptors for this request. The choice may be made
     * on request URL, session state, or any factor the implementing class chooses.
     * <p>The returned HandlerExecutionChain contains a handler Object, rather than
     * even a tag interface, so that handlers are not constrained in any way.
     * For example, a HandlerAdapter could be written to allow another framework's
     * handler objects to be used.
     * <p>Returns {@code null} if no match was found. This is not an error.
     * The DispatcherServlet will query all registered HandlerMapping beans to find
     * a match, and only decide there is an error if none can find a handler.
     *
     * @param request current HTTP request
     * @return a HandlerExecutionChain instance containing handler object and
     * any interceptors, or {@code null} if no mapping found
     * @throws Exception if there is an internal error
     */
    public HandlerExecutionChain getHandler(HttpServletRequest request) throws Exception {
        String url = UrlPathHelper.getRequestUrl(request);
        if (this.pattern.matcher(url).matches()) {
            HandlerExecutionChain chain = new HandlerExecutionChain(this.handler);
            Collection<HandlerInterceptor> handlerInterceptors = this.context.getBeansOfType(HandlerInterceptor.class).values();
            for (HandlerInterceptor handlerInterceptor : handlerInterceptors) {
                chain.addInterceptor(handlerInterceptor);
            }
            return chain;
        }
        return null;
    }

    @Override
    public int compare(HandlerMapping o1, HandlerMapping o2) {
        Pattern p1 = o1.getPattern();
        Pattern p2 = o2.getPattern();
        return p1.pattern().compareTo(p2.pattern());
    }
}
