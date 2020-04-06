package MySpringMVC.V2.webmvc.servlet.mvc;

import MySpringMVC.V2.webmvc.servlet.ModelAndView;

import java.lang.reflect.Method;

public class UrlFileNameViewController implements Controller {

    private final Object controller;

    public UrlFileNameViewController(Object controller) {
        this.controller = controller;
    }

    @Override
    public ModelAndView handleRequest(Method method, Object[] args) throws Exception {
        Object result = method.invoke(this.controller, args);
        if (result == null || result instanceof Void) {
            return null;
        }
        if (result instanceof ModelAndView) {
            return (ModelAndView) result;
        }
        return new ModelAndView(result);
    }
}
