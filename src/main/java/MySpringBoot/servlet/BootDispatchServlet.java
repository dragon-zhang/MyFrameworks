package MySpringBoot.servlet;

import MySpringMVC.annotation.*;
import MySpringMVC.pojo.MyMethod;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author SuccessZhang
 */
public class BootDispatchServlet extends HttpServlet {

    private String scanPackage;

    public BootDispatchServlet(String scanPackage) {
        this.scanPackage = scanPackage;
    }

    private List<String> classNames = new ArrayList<>();

    private Map<String, Object> ioc = new HashMap<>();

    private Map<String, MyMethod> handlerMapping = new HashMap<>();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doDispatch(req, resp);
    }

    private void doDispatch(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String uri = req.getRequestURI();
        String contextPath = req.getContextPath();
        uri = uri.replace(contextPath, "").replaceAll("/+", "/");
        if (handlerMapping.containsKey(uri)) {
            MyMethod handler = handlerMapping.get(uri);
            Method method = handler.getMethod();
            System.out.println(method);
            Map<String, String[]> params = req.getParameterMap();

            Class<?>[] paramsTypes = method.getParameterTypes();
            Object[] paramsValues = new Object[paramsTypes.length];
            paramsValues[0] = req;
            paramsValues[1] = resp;
            for (int i = 0; i < handler.getParams().size(); i++) {
                Map<String, Parameter> map = handler.getParams().get(i);
                String paramName = map.keySet().toArray(new String[0])[0];
                Parameter parameter = map.get(paramName);
                String[] values = params.get(paramName);
                if (values.length == 1) {
                    String type = parameter.getType().getName();
                    if (type.endsWith("int") | type.endsWith("Integer")) {
                        paramsValues[i + 2] = Integer.valueOf(values[0]);
                    } else if (type.endsWith("String")) {
                        paramsValues[i + 2] = values[0];
                    } else if (type.endsWith("char") | type.endsWith("Character")) {
                        paramsValues[i + 2] = values[0].toCharArray()[0];
                    } else if (type.endsWith("boolean") | type.endsWith("Boolean")) {
                        paramsValues[i + 2] = Boolean.valueOf(values[0]);
                    } else if (type.endsWith("byte") | type.endsWith("Byte")) {
                        paramsValues[i + 2] = values[0].getBytes()[0];
                    } else if (type.endsWith("float") | type.endsWith("Float")) {
                        paramsValues[i + 2] = Float.valueOf(values[0]);
                    } else if (type.endsWith("double") | type.endsWith("Double")) {
                        paramsValues[i + 2] = Double.valueOf(values[0]);
                    } else if (type.endsWith("short") | type.endsWith("Short")) {
                        paramsValues[i + 2] = Short.valueOf(values[0]);
                    }
                } else if (values.length > 1) {
                    paramsValues[i + 2] = values;
                }
            }

            String beanName = lowerFirstCase(method.getDeclaringClass().getSimpleName());
            try {
                method.invoke(ioc.get(beanName), paramsValues);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        long start = System.currentTimeMillis();
        //1.解析配置，扫描配置包下的所有类
        doScan(scanPackage);
        //2.初始化扫描到的所有类并且放入IOC容器中
        doInitInstance();
        //3.完成自动化注入的DI操作
        doAutowired();
        //4.初始化HandlerMapping、将URL和Method一一对应
        doInitHandlerMapping();
        long end = System.currentTimeMillis();
        System.out.println("MySpringBoot init successfully ! cost time:" + (end - start));
    }

    private void doScan(String scanPackage) {
        //此时只能拿到字符串
        URL url = this.getClass().getClassLoader().getResource("/" + scanPackage.replaceAll("\\.", "/"));
        if (url == null) {
            throw new RuntimeException("url not found!");
        }
        File dir = new File(url.getFile());
        File[] files = dir.listFiles();
        if (files == null) {
            throw new RuntimeException("there is no file!");
        }
        for (File file : files) {
            if (file.isDirectory()) {
                doScan(scanPackage + "." + file.getName());
            } else {
                String className = scanPackage + "." + file.getName().replace(".class", "");
                classNames.add(className);
            }
        }
    }

    private void doInitInstance() {
        if (classNames.isEmpty()) {
            throw new RuntimeException("there is no class in this package!");
        }
        try {
            //只有加了@Controller、@Service、@Component的类才需要初始化
            for (String className : classNames) {
                Class<?> clazz = Class.forName(className);
                if (clazz.isAnnotationPresent(Controller.class)) {
                    String beanName = lowerFirstCase(clazz.getSimpleName());
                    if (ioc.containsKey(beanName)) {
                        throw new RuntimeException("the beanName:" + beanName + " is defined!");
                    }
                    if (!"".equals(beanName)) {
                        ioc.put(beanName, clazz.newInstance());
                    }
                } else if (clazz.isAnnotationPresent(Service.class)) {
                    String beanName = clazz.getAnnotation(Service.class).value();
                    if (!"".equals(beanName.trim())) {
                        beanName = lowerFirstCase(beanName);
                    } else {
                        beanName = lowerFirstCase(clazz.getSimpleName());
                    }
                    if (ioc.containsKey(beanName)) {
                        throw new RuntimeException("the beanName:" + beanName + " is defined!");
                    }
                    if (!"".equals(beanName)) {
                        ioc.put(beanName, clazz.newInstance());
                    }

                    Class<?>[] interfaces = clazz.getInterfaces();
                    for (Class<?> i : interfaces) {
                        if (ioc.containsKey(i.getName())) {
                            throw new RuntimeException("the beanName:" + beanName + " is defined!");
                        }
                        String interfaceName = i.getName();
                        if (!"".equals(interfaceName)) {
                            ioc.put(interfaceName, clazz.newInstance());
                        }
                    }
                } else if (clazz.isAnnotationPresent(Component.class)) {
                    String beanName = lowerFirstCase(clazz.getSimpleName());
                    if (ioc.containsKey(beanName)) {
                        throw new RuntimeException("the beanName:" + beanName + " is defined!");
                    }
                    if (!"".equals(beanName)) {
                        ioc.put(beanName, clazz.newInstance());
                    }
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }

    private String lowerFirstCase(String simpleName) {
        //beanName首字母小写，原理ASCII码
        char[] chars = simpleName.toCharArray();
        if (65 <= chars[0] && chars[0] <= 90) {
            chars[0] += 32;
        }
        return String.valueOf(chars);
    }

    private void doAutowired() {
        if (ioc.isEmpty()) {
            throw new RuntimeException("ioc is empty!");
        }
        for (Map.Entry<String, Object> entry : ioc.entrySet()) {
            Field[] fields = entry.getValue().getClass().getDeclaredFields();
            for (Field field : fields) {
                if (!field.isAnnotationPresent(Autowired.class)) {
                    continue;
                }
                Autowired autowired = field.getAnnotation(Autowired.class);
                String beanName = autowired.value().trim();
                if ("".equals(beanName)) {
                    beanName = field.getType().getName();
                }
                //允许强制赋值
                field.setAccessible(true);
                try {
                    field.set(entry.getValue(), ioc.get(beanName));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void doInitHandlerMapping() {
        if (ioc.isEmpty()) {
            throw new RuntimeException("ioc is empty!");
        }
        for (Map.Entry<String, Object> entry : ioc.entrySet()) {
            Class<?> clazz = entry.getValue().getClass();
            if (!clazz.isAnnotationPresent(Controller.class)) {
                continue;
            }
            String baseUrl = "";
            if (clazz.isAnnotationPresent(RequestMapping.class)) {
                RequestMapping requestMapping = clazz.getAnnotation(RequestMapping.class);
                baseUrl = baseUrl + requestMapping.value();
            }

            Method[] methods = clazz.getMethods();
            for (Method method : methods) {
                if (!method.isAnnotationPresent(RequestMapping.class)) {
                    continue;
                }
                RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
                String url = (baseUrl + "/" + requestMapping.value()).replaceAll("/+", "/");
                MyMethod handler = new MyMethod(method);
                Parameter[] parameters = method.getParameters();
                for (Parameter parameter : parameters) {
                    if (!parameter.isAnnotationPresent(RequestParam.class)) {
                        continue;
                    }
                    RequestParam requestParam = parameter.getAnnotation(RequestParam.class);
                    String parameterName = requestParam.value().trim();
                    if ("".equals(parameterName)) {
                        parameterName = lowerFirstCase(parameter.getName());
                    }
                    Map<String, Parameter> map = new HashMap<>(1);
                    map.put(parameterName, parameter);
                    handler.getParams().add(map);
                }
                handlerMapping.put(url, handler);
                System.out.println("Mapped : " + url + " : " + method);
            }
        }
    }
}