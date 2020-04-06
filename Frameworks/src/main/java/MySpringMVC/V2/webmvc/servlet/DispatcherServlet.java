package MySpringMVC.V2.webmvc.servlet;

import MySpringMVC.V2.annotation.Controller;
import MySpringMVC.V2.annotation.RequestMapping;
import MySpringMVC.V2.beans.config.BeanDefinition;
import MySpringMVC.V2.beans.support.BeanDefinitionReader;
import MySpringMVC.V2.beans.support.DefaultListableBeanFactory;
import MySpringMVC.V2.context.ApplicationContextAware;
import MySpringMVC.V2.context.ClassPathXmlApplicationContext;
import MySpringMVC.V2.context.support.AbstractApplicationContext;
import MySpringMVC.V2.webmvc.servlet.mvc.SimpleControllerHandlerAdapter;
import MySpringMVC.V2.webmvc.servlet.support.HandlerRuntimeExceptionResolver;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @author SuccessZhang
 * @date 2020/04/03
 */
public class DispatcherServlet extends HttpServlet implements ApplicationContextAware {

    private static final long serialVersionUID = 6632851494428599989L;

    private static final String CONTEXT_CONFIG_LOCATION = "contextConfigLocation";

    private AbstractApplicationContext applicationContext;

    private List<HandlerMapping> handlerMappings = new ArrayList<>();

    private List<HandlerAdapter> handlerAdapters = new ArrayList<>();

    private List<HandlerExceptionResolver> handlerExceptionResolvers = new ArrayList<>();

    private List<ViewResolver> viewResolvers = new ArrayList<>();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            this.doDispatch(req, resp);
        } catch (Exception e) {
            e.printStackTrace();
            try {
                Map<String, Object> model = new HashMap<>(2);
                model.put("detail", e.getMessage());
                model.put("stackTrace", Arrays.toString(e.getStackTrace()));
                processDispatchResult(req, resp, null, new ModelAndView("500", model), e);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        this.applicationContext = new ClassPathXmlApplicationContext(config.getInitParameter(CONTEXT_CONFIG_LOCATION));
        initStrategies(this.applicationContext);
    }

    @Override
    public void setApplicationContext(AbstractApplicationContext applicationContext) throws Exception {
        this.applicationContext = applicationContext;
        initStrategies(this.applicationContext);
    }

    /**
     * Initialize the strategy objects that this servlet uses.
     * <p>May be overridden in subclasses in order to initialize further strategy objects.
     */
    private void initStrategies(AbstractApplicationContext context) {
        //多文件上传的组件
        initMultipartResolver(context);
        //初始化本地语言环境
        initLocaleResolver(context);
        //初始化模板处理器
        initThemeResolver(context);
        //初始化映射处理器，实现
        initHandlerMappings(context);
        //初始化参数适配器，实现
        initHandlerAdapters(context);
        //初始化异常拦截器，实现
        initHandlerExceptionResolvers(context);
        //初始化视图预处理器
        initRequestToViewNameTranslator(context);
        //初始化视图转换器
        initViewResolvers(context);
        //初始化HTTP会话组件（保存重要信息、失败重连）
        initFlashMapManager(context);
    }

    private void initMultipartResolver(AbstractApplicationContext context) {
    }

    private void initLocaleResolver(AbstractApplicationContext context) {
    }

    private void initThemeResolver(AbstractApplicationContext context) {
    }

    /**
     * Initialize the HandlerMappings used by this class.
     * <p>If no HandlerMapping beans are defined in the BeanFactory for this namespace,
     * we default to BeanNameUrlHandlerMapping.
     */
    private void initHandlerMappings(AbstractApplicationContext context) {
        try {
            DefaultListableBeanFactory beanFactory = context.getBeanFactory();
            for (String beanDefinitionName : beanFactory.getBeanDefinitionNames()) {
                BeanDefinition beanDefinition = beanFactory.getBeanDefinition(beanDefinitionName);
                Class<?> beanClass = beanDefinition.getBeanClass();
                Object bean = context.getBean(beanDefinition.getFactoryBeanName());
                if (!beanClass.isAnnotationPresent(Controller.class)) {
                    continue;
                }
                String baseUrl = "";
                if (beanClass.isAnnotationPresent(RequestMapping.class)) {
                    RequestMapping requestMapping = beanClass.getAnnotation(RequestMapping.class);
                    baseUrl = baseUrl + requestMapping.value();
                }

                Method[] methods = beanClass.getDeclaredMethods();
                for (Method method : methods) {
                    if (!method.isAnnotationPresent(RequestMapping.class)) {
                        continue;
                    }
                    RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
                    String regex = (baseUrl + "/" + requestMapping.value().replaceAll("\\*", ".*"))
                            .replaceAll("/+", "/");
                    Pattern pattern = Pattern.compile(regex);
                    this.handlerMappings.add(new HandlerMapping(context, pattern, bean, method));
                    System.out.println("Mapped : " + regex + " : " + method);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Initialize the HandlerAdapters used by this class.
     * <p>If no HandlerAdapter beans are defined in the BeanFactory for this namespace,
     * we default to SimpleControllerHandlerAdapter.
     */
    private void initHandlerAdapters(AbstractApplicationContext context) {
        this.handlerAdapters.add(new SimpleControllerHandlerAdapter(this.handlerMappings));
    }

    /**
     * Initialize the HandlerExceptionResolver used by this class.
     * <p>If no bean is defined with the given name in the BeanFactory for this namespace,
     * we default to no exception resolver.
     */
    private void initHandlerExceptionResolvers(AbstractApplicationContext context) {
        handlerExceptionResolvers.add(new HandlerRuntimeExceptionResolver());
        handlerExceptionResolvers.add(new HandlerExceptionResolver());
    }

    private void initRequestToViewNameTranslator(AbstractApplicationContext context) {
    }

    /**
     * Initialize the ViewResolvers used by this class.
     * <p>If no ViewResolver beans are defined in the BeanFactory for this
     * namespace, we default to InternalResourceViewResolver.
     */
    private void initViewResolvers(AbstractApplicationContext context) {
        String templateLocation = context.getConfig().getProperty(BeanDefinitionReader.TEMPLATE_LOADER_PATH);
        URL url = this.getClass().getClassLoader().getResource(templateLocation);
        if (url == null) {
            return;
        }
        String templateRootPath = url.getFile();
        File templateRootDir = new File(templateRootPath);
        String[] templatePaths = templateRootDir.list();
        if (templatePaths == null) {
            return;
        }
        for (String templatePath : templatePaths) {
            this.viewResolvers.add(new ViewResolver(templateRootPath + templatePath,
                    applicationContext.getConfig().getProperty(BeanDefinitionReader.DEFAULT_ENCODING)));
        }
    }

    private void initFlashMapManager(AbstractApplicationContext context) {
    }

    private void doDispatch(HttpServletRequest request, HttpServletResponse response) throws Exception {
        HandlerExecutionChain mappedHandler = null;
        ModelAndView mv = null;
        Exception dispatchException = null;
        try {
            mappedHandler = getHandler(request);
            if (mappedHandler == null) {
                noHandlerFound(request, response);
                return;
            }
            HandlerAdapter handlerAdapter = getHandlerAdapter(mappedHandler.getHandler());
            if (!mappedHandler.applyPreHandle(request, response)) {
                return;
            }
            mv = handlerAdapter.handle(request, response, mappedHandler.getHandler());
            //结果视图对象的处理
            mappedHandler.applyPostHandle(request, response, mv);
        } catch (Exception e) {
            dispatchException = e;
        }
        processDispatchResult(request, response, mappedHandler, mv, dispatchException);
    }

    private HandlerExecutionChain getHandler(HttpServletRequest request) throws Exception {
        for (HandlerMapping hm : this.handlerMappings) {
            HandlerExecutionChain handler = hm.getHandler(request);
            if (handler != null) {
                return handler;
            }
        }
        return null;
    }

    private HandlerAdapter getHandlerAdapter(Object handler) throws ServletException {
        if (!this.handlerAdapters.isEmpty()) {
            for (HandlerAdapter ha : this.handlerAdapters) {
                if (ha.supports(handler)) {
                    return ha;
                }
            }
        }
        throw new ServletException("No adapter for handler [" + handler + "]: The DispatcherServlet configuration needs to include a HandlerAdapter that supports this handler");
    }

    private void processDispatchResult(HttpServletRequest request, HttpServletResponse response, HandlerExecutionChain mappedHandler, ModelAndView mv, Exception exception) throws Exception {
        if (exception != null) {
            Object handler = (mappedHandler != null ? mappedHandler.getHandler() : null);
            mv = processHandlerException(request, response, handler, exception);
        }
        if (mv == null) {
            return;
        }
        if (this.viewResolvers.isEmpty()) {
            return;
        }
        for (ViewResolver viewResolver : this.viewResolvers) {
            View view = viewResolver.resolveViewName(mv.getViewName(), Locale.SIMPLIFIED_CHINESE);
            if (view == null) {
                continue;
            }
            view.render(mv.getModel(), request, response);
        }
        if (mappedHandler != null) {
            mappedHandler.triggerAfterCompletion(request, response, null);
        }
    }

    private ModelAndView processHandlerException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception exception) throws Exception {
        if (this.handlerExceptionResolvers != null) {
            for (HandlerExceptionResolver handlerExceptionResolver : this.handlerExceptionResolvers) {
                if (!handlerExceptionResolver.support(exception)) {
                    continue;
                }
                ModelAndView exMv = handlerExceptionResolver.resolveException(request, response, handler, exception);
                if (exMv != null) {
                    return exMv;
                }
            }
        }
        throw exception;
    }

    private void noHandlerFound(HttpServletRequest request, HttpServletResponse response) throws Exception {
        processDispatchResult(request, response, null, new ModelAndView("404"), null);
    }
}
