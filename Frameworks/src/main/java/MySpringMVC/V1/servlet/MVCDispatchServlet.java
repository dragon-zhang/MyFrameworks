package MySpringMVC.V1.servlet;

import MyJedis.MyJedis;
import MyMybatis.factory.SqlSessionFactory;
import MyMybatis.proxy.InterfaceProxy;
import MyMybatis.session.SqlSession;
import MySpringMVC.V1.annotation.Autowired;
import MySpringMVC.V1.annotation.Component;
import MySpringMVC.V1.annotation.Controller;
import MySpringMVC.V1.annotation.Repository;
import MySpringMVC.V1.annotation.RequestMapping;
import MySpringMVC.V1.annotation.RequestParam;
import MySpringMVC.V1.annotation.Service;
import MySpringMVC.V1.pojo.MyMethod;
import com.mysql.jdbc.StringUtils;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

/**
 * @author SuccessZhang
 */
public class MVCDispatchServlet extends HttpServlet {

    private Properties contextConfig = new Properties();

    private List<String> classNames = new ArrayList<>();

    private Map<String, Object> ioc = new HashMap<>();

    private Map<String, MyMethod> handlerMapping = new HashMap<>();

    private SqlSession session;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doDispatch(req, resp);
    }

    private void doDispatch(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String ip = req.getRemoteAddr();
        String sessionId = MyJedis.get(ip);
        if (StringUtils.isNullOrEmpty(sessionId)) {
            //解决分布式的session一致性问题
            HttpSession session = req.getSession();
            MyJedis.set(ip, session.getId());
            MyJedis.expire(ip, Integer.valueOf(contextConfig.getProperty("redis.expire.session")));
            sessionId = session.getId();
        }
        String uri = req.getRequestURI();
        String contextPath = req.getContextPath();
        uri = uri.replace(contextPath, "").replaceAll("/+", "/");
        if (!handlerMapping.containsKey(uri)) {
            resp.getWriter().write("ip:" + ip + ",session:" + sessionId + ",not found! tomcat8.5.37");
        } else {
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
        //1.加载配置文件
        doLoadConfig(config.getInitParameter("contextConfigLocation"));
        SqlSessionFactory factory = new SqlSessionFactory(contextConfig);
        session = factory.openSession();
        //2.解析配置，扫描配置包下的所有类
        doScan(contextConfig.getProperty("base-package"));
        //3.初始化扫描到的所有类并且放入IOC容器中
        doInitInstance();
        //4.完成自动化注入的DI操作
        doAutowired();
        //5.初始化HandlerMapping、将URL和Method一一对应
        doInitHandlerMapping();
        long end1 = System.currentTimeMillis();
        System.out.println("MySpringMVC init successfully ! cost time:" + (end1 - start));
        long end2 = System.currentTimeMillis();
        MyJedis.init(contextConfig.getProperty("redis.host"), Integer.valueOf(contextConfig.getProperty("redis.port")));
        System.out.println("MyJedis init successfully ! cost time:" + (end2 - end1));
        System.out.println("--------------------------------------------------------------------------------");
        System.out.println("Tomcat首先加载META-INF中service里配置的实现了ServletContainerInitializer接口的类");
        System.out.println("但是会优先加载web.xml中配置的servlet");
        System.out.println("然后才加载ServletContainerInitializer实现类的onStartup()方法中配置的servlet");
        System.out.println("mvc被先加载,boot后加载");
        System.out.println("--------------------------------------------------------------------------------");
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

    @SuppressWarnings("unchecked")
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
                    if (beanName.endsWith("Mapper") | beanName.endsWith("Dao")) {
                        field.set(entry.getValue(), session.getMapper(((InterfaceProxy) ioc.get(beanName)).getMapperInterface()));
                    } else {
                        field.set(entry.getValue(), ioc.get(beanName));
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
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
                } else if (clazz.isAnnotationPresent(Repository.class)) {
                    String beanName = clazz.getAnnotation(Repository.class).value();
                    if (!"".equals(beanName.trim())) {
                        beanName = lowerFirstCase(beanName);
                    } else {
                        beanName = lowerFirstCase(clazz.getName());
                    }
                    if (ioc.containsKey(beanName)) {
                        throw new RuntimeException("the beanName:" + beanName + " is defined!");
                    }
                    if (!"".equals(beanName)) {
                        InterfaceProxy factory = new InterfaceProxy<>();
                        factory.setMapperInterface(clazz);
                        ioc.put(beanName, factory);
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

    private void doLoadConfig(String contextConfigLocation) {
        contextConfigLocation = contextConfigLocation.replace("classpath:", "")
                .replace("classpath*:", "");
        String prefix = Objects.requireNonNull(this.getClass().getClassLoader().getResource("")).getPath();
        try (InputStream inputStream = new FileInputStream(prefix + contextConfigLocation)) {
            contextConfig.load(inputStream);
            contextConfig.setProperty("prefix", prefix);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}