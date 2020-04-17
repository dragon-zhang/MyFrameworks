package MySpringMVC.V2.beans.support;

import MySpringMVC.V2.annotation.Bean;
import MySpringMVC.V2.annotation.Component;
import MySpringMVC.V2.annotation.Lazy;
import MySpringMVC.V2.annotation.Scope;
import MySpringMVC.V2.aop.aspectj.AbstractAspectJAdvice;
import MySpringMVC.V2.aop.aspectj.AspectJAfterAdvice;
import MySpringMVC.V2.aop.aspectj.AspectJAfterReturningAdvice;
import MySpringMVC.V2.aop.aspectj.AspectJAfterThrowingAdvice;
import MySpringMVC.V2.aop.aspectj.AspectJAroundAdvice;
import MySpringMVC.V2.aop.aspectj.AspectJExpressionPointcut;
import MySpringMVC.V2.aop.aspectj.AspectJMethodBeforeAdvice;
import MySpringMVC.V2.aop.aspectj.AspectJPointcutAdvisor;
import MySpringMVC.V2.beans.config.BeanDefinition;
import MySpringMVC.V2.beans.config.ConfigurableBeanFactory;
import MySpringMVC.V2.core.AnnotationUtils;
import MySpringMVC.V2.core.CloneUtils;
import MySpringMVC.V2.core.StringUtils;
import com.alibaba.fastjson.JSON;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class BeanDefinitionReader {

    public static final String TEMPLATE_LOADER_PATH = "templateLoaderPath";

    public static final String DEFAULT_ENCODING = "defaultEncoding";

    public static final String BASE_PACKAGE = "base-package";

    private static final String NAME = "name";

    private static final String VALUE = "value";

    private static final String CLASSPATH_URL_PREFIX = "classpath:";

    private static final String CLASSPATH_ALL_URL_PREFIX = "classpath*:";

    private static final String PROPERTIES = ".properties";

    private static final String XML = ".xml";

    private final Properties config = new Properties();

    private final List<String> beanClassNames = new ArrayList<>();

    public BeanDefinitionReader(String[] locations) {
        ClassLoader loader = this.getClass().getClassLoader();
        for (String fileName : locations) {
            fileName = fileName.replace(CLASSPATH_URL_PREFIX, "")
                    .replace(CLASSPATH_ALL_URL_PREFIX, "");
            if (fileName.endsWith(PROPERTIES)) {
                loadProperties(loader, fileName);
            } else if (fileName.endsWith(XML)) {
                loadXml(loader, fileName);
            }
        }
        doScanBasePackage(config.getProperty(BASE_PACKAGE));
    }

    @SuppressWarnings("unchecked")
    private void loadXml(ClassLoader loader, String fileName) {
        try (InputStream inputStream = loader.getResourceAsStream(fileName)) {
            SAXReader reader = new SAXReader();
            Document document = reader.read(inputStream);
            //获取根节点<beans>
            Element root = document.getRootElement();
            //获取子节点列表
            List<Element> elements = root.elements();
            //获取basePackage
            String basePackage = elements.get(0).attribute(BASE_PACKAGE).getData().toString();
            config.setProperty(BASE_PACKAGE, basePackage);
            //继续获取模板相关配置
            List<Element> template = elements.get(1).elements();
            for (Element property : template) {
                setProperty(property, TEMPLATE_LOADER_PATH);
                setProperty(property, DEFAULT_ENCODING);
            }
            List<AspectJPointcutAdvisor> advisors = new ArrayList<>(5);
            AspectJPointcutAdvisor advisor;
            AbstractAspectJAdvice advice;
            Class<?> beanClass = null;
            Element aspectClass = elements.get(2);
            String id = aspectClass.attribute("id").getData().toString();
            String className = aspectClass.attribute("class").getData().toString();
            List<Element> aspects = elements.get(3).elements();
            Element aspect = aspects.get(0);
            String ref = aspect.attribute("ref").getData().toString();
            int order = Integer.parseInt(aspect.attribute("order").getData().toString());
            if (id.equals(ref)) {
                beanClass = Class.forName(className);
            }

            List<Element> pointcutAndAdvices = aspect.elements();
            Element pointcut = pointcutAndAdvices.get(0);
            String pointcutId = pointcut.attribute("id").getData().toString();
            String expression = pointcut.attribute("expression").getData().toString();
            AspectJExpressionPointcut expressionPointcut = new AspectJExpressionPointcut(pointcutId, expression);

            Element before = pointcutAndAdvices.get(1);
            String methodName = before.attribute("method").getData().toString();
            String pointcutRef = before.attribute("pointcut-ref").getData().toString();
            if (pointcutRef.equals(pointcutId)) {
                advisor = new AspectJPointcutAdvisor();
                advice = new AspectJMethodBeforeAdvice(beanClass, methodName, CloneUtils.clone(expressionPointcut), order);
                advice.setAspectName(ref);
                advisor.setAdvice(advice);
                advisors.add(advisor);
            }

            Element after = pointcutAndAdvices.get(2);
            methodName = after.attribute("method").getData().toString();
            pointcutRef = after.attribute("pointcut-ref").getData().toString();
            if (pointcutRef.equals(pointcutId)) {
                advisor = new AspectJPointcutAdvisor();
                advice = new AspectJAfterAdvice(beanClass, methodName, CloneUtils.clone(expressionPointcut), order);
                advice.setAspectName(ref);
                advisor.setAdvice(advice);
                advisors.add(advisor);
            }

            Element around = pointcutAndAdvices.get(3);
            methodName = around.attribute("method").getData().toString();
            pointcutRef = around.attribute("pointcut-ref").getData().toString();
            if (pointcutRef.equals(pointcutId)) {
                advisor = new AspectJPointcutAdvisor();
                advice = new AspectJAroundAdvice(beanClass, methodName, CloneUtils.clone(expressionPointcut), order);
                advice.setAspectName(ref);
                advisor.setAdvice(advice);
                advisors.add(advisor);
            }

            Element afterReturning = pointcutAndAdvices.get(4);
            methodName = afterReturning.attribute("method").getData().toString();
            pointcutRef = afterReturning.attribute("pointcut-ref").getData().toString();
            if (pointcutRef.equals(pointcutId)) {
                advisor = new AspectJPointcutAdvisor();
                advice = new AspectJAfterReturningAdvice(beanClass, methodName, CloneUtils.clone(expressionPointcut), order);
                advice.setAspectName(ref);
                String returning = afterReturning.attribute("returning").getData().toString();
                advice.setReturningName(returning);
                advisor.setAdvice(advice);
                advisors.add(advisor);
            }

            Element afterThrowing = pointcutAndAdvices.get(5);
            methodName = afterThrowing.attribute("method").getData().toString();
            pointcutRef = afterThrowing.attribute("pointcut-ref").getData().toString();
            if (pointcutRef.equals(pointcutId)) {
                advisor = new AspectJPointcutAdvisor();
                advice = new AspectJAfterThrowingAdvice(beanClass, methodName, CloneUtils.clone(expressionPointcut), order);
                advice.setAspectName(ref);
                String throwing = afterThrowing.attribute("throwing").getData().toString();
                advice.setThrowingName(throwing);
                advisor.setAdvice(advice);
                advisors.add(advisor);
            }

            //fastjson序列化的坑，可以序列化进去，但是反序列化出来如果用基类，无法获取子类
            config.setProperty("advisors", JSON.toJSONString(advisors));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void setProperty(Element property, String propertyName) {
        if (property.attribute(NAME).getData().toString().equals(propertyName)) {
            config.setProperty(propertyName, property.attribute(VALUE).getData().toString());
        }
    }

    private void loadProperties(ClassLoader loader, String fileName) {
        try (InputStream inputStream = loader.getResourceAsStream(fileName)) {
            config.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void doScanBasePackage(String basePackage) {
        //此时只能拿到字符串
        URL url = this.getClass().getResource("/" + basePackage.replaceAll("\\.", "/"));
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
                doScanBasePackage(basePackage + "." + file.getName());
            } else {
                String className = basePackage + "." + file.getName().replace(".class", "");
                beanClassNames.add(className);
            }
        }
    }

    public Properties getConfig() {
        return config;
    }

    public List<BeanDefinition> loadBeanDefinitions() throws Exception {
        List<BeanDefinition> beanDefinitions = new ArrayList<>();
        for (String beanClassName : beanClassNames) {
            Class<?> beanClass = Class.forName(beanClassName);
            Annotation annotation = AnnotationUtils.getCoveredAnnotation(beanClass);
            if (annotation == null) {
                continue;
            }
            String factoryBeanName = "";
            boolean autowire = true;
            String initMethod = null;
            String destroyMethod = null;
            if (annotation instanceof Component) {
                factoryBeanName = ((Component) annotation).value();
            }
            if (annotation instanceof Bean) {
                Bean bean = (Bean) annotation;
                factoryBeanName = bean.value();
                autowire = bean.autowire();
                initMethod = bean.initMethod();
                destroyMethod = bean.destroyMethod();
            }
            if ("".equals(factoryBeanName)) {
                factoryBeanName = StringUtils.lowerFirstCase(beanClass.getSimpleName());
            }
            BeanDefinition beanDefinition = doCreateBeanDefinition(beanClassName, beanClass, factoryBeanName, autowire, initMethod, destroyMethod);
            Class<?>[] interfaces = beanClass.getInterfaces();
            beanDefinitions.add(beanDefinition);
            for (Class<?> i : interfaces) {
                String interfaceName = StringUtils.lowerFirstCase(i.getSimpleName());
                if (!"".equals(interfaceName)) {
                    beanDefinition = doCreateBeanDefinition(beanClassName, beanClass, interfaceName, autowire, initMethod, destroyMethod);
                    beanDefinitions.add(beanDefinition);
                }
            }
        }
        return beanDefinitions;
    }

    private BeanDefinition doCreateBeanDefinition(String beanClassName, Class<?> beanClass, String beanName, boolean autowire, String initMethod, String destroyMethod) {
        String scopeName = doGetScopeName(beanClass);
        BeanDefinition beanDefinition = new BeanDefinition(scopeName, beanName, beanClassName, beanClass, autowire, initMethod, destroyMethod);
        doSetLazyInit(beanClass, beanDefinition);
        return beanDefinition;
    }

    private void doSetLazyInit(Class<?> beanClass, BeanDefinition beanDefinition) {
        Lazy lazy = beanClass.getAnnotation(Lazy.class);
        if (lazy != null) {
            beanDefinition.setLazyInit(lazy.value());
        }
    }

    private String doGetScopeName(Class<?> beanClass) {
        String scopeName = ConfigurableBeanFactory.SCOPE_SINGLETON;
        Scope scope = beanClass.getAnnotation(Scope.class);
        if (scope != null) {
            scopeName = scope.scopeName();
        }
        switch (scopeName) {
            case ConfigurableBeanFactory.SCOPE_SINGLETON:
            case ConfigurableBeanFactory.SCOPE_PROTOTYPE:
                break;
            default:
                throw new RuntimeException("bean scope error !");
        }
        return scopeName;
    }
}
