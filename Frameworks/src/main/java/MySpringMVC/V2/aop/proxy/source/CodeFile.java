package MySpringMVC.V2.aop.proxy.source;

import MySpringMVC.V2.aop.proxy.ProxyHelper;
import MySpringMVC.V2.aop.proxy.cglib.MethodInterceptor;
import MySpringMVC.V2.aop.proxy.cglib.MethodProxy;
import MySpringMVC.V2.aop.proxy.jdk.InvocationHandler;
import MySpringMVC.V2.aop.proxy.loader.ProxyClassLoader;

import javax.tools.JavaFileObject;
import javax.tools.SimpleJavaFileObject;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

/**
 * 存储java源代码
 *
 * @author SuccessZhang
 * @date 2020/04/11
 */
public class CodeFile extends SimpleJavaFileObject {

    private static final JavaFileObject.Kind JAVA_SOURCE_FILE = JavaFileObject.Kind.SOURCE;

    private String src;

    public CodeFile(Class<?>[] interfaces) throws URISyntaxException {
        this(new URI(ProxyHelper.PROXY_CLASS_PREFIX + ProxyHelper.getProxyClassCount() + JAVA_SOURCE_FILE.extension), JAVA_SOURCE_FILE, interfaces);
    }

    private CodeFile(URI uri, Kind kind, Class<?>[] interfaces) {
        super(uri, kind);
        this.src = generateSrc(interfaces);
    }

    /**
     * jdk
     */
    private String generateSrc(Class<?>[] interfaces) {
        //利用反射生成java源代码
        StringBuilder sb = new StringBuilder();
        sb.append("package ").append(ProxyClassLoader.class.getPackage().getName()).append(";\n");
        sb.append("import ").append(InvocationHandler.class.getName()).append(";\n");
        for (Class<?> i : interfaces) {
            sb.append("import ").append(i.getName()).append(";\n");
        }
        sb.append("import java.lang.reflect.*;\n");
        sb.append("public class $Proxy").append(ProxyHelper.getProxyClassCount()).append(" implements ");
        for (Class<?> i : interfaces) {
            sb.append(i.getSimpleName());
            sb.append(",");
        }
        deleteRedundantChar(sb, "");
        sb.append(" {\n");
        sb.append("private InvocationHandler h;\n");
        sb.append("public $Proxy").append(ProxyHelper.getProxyClassCount()).append("(InvocationHandler h){\nthis.h=h;\n}\n");
        for (Class<?> i : interfaces) {
            for (Method method : i.getMethods()) {
                Class<?> returnType = method.getReturnType();
                String returnTypeName = returnType.getName();
                sb.append("@Override\n");
                sb.append(Modifier.toString(method.getModifiers()).replace(" abstract", "")).append(" ");
                sb.append(returnTypeName).append(" ");
                sb.append(method.getName()).append("(");
                for (Parameter parameter : method.getParameters()) {
                    Class<?> paramType = parameter.getType();
                    String paramTypeName = paramType.getName();
                    sb.append(paramTypeName).append(" ").append(parameter.getName());
                    sb.append(",");
                }
                deleteRedundantChar(sb, "(");
                sb.append(")").append(" {\n");

                sb.append("Object $result = null;\n");
                sb.append("try{\n");
                sb.append("Method m = ").append(method.getDeclaringClass().getName()).append(".class.getMethod(\"").append(method.getName()).append("\",");
                for (Parameter parameter : method.getParameters()) {
                    sb.append(parameter.getType().getName()).append(".class");
                    sb.append(",");
                }
                deleteRedundantChar(sb, "(");
                sb.append(");\n");

                sb.append("$result = this.h.invoke(this,m,new Object[]{");
                for (Parameter parameter : method.getParameters()) {
                    sb.append(parameter.getName());
                    sb.append(",");
                }
                deleteRedundantChar(sb, "{");

                sb.append("});\n");
                sb.append("}catch (Throwable t) {\nt.printStackTrace();\n}\n");
                if (!"java.lang.Void".equals(returnTypeName) && !"void".equals(returnTypeName)) {
                    sb.append("return (").append(returnTypeName).append(")$result;\n");
                }
                sb.append("}\n");
            }
        }

        sb.append("}\n");
        System.out.println(sb.toString());
        return sb.toString();
    }

    public CodeFile(Class<?> type) throws URISyntaxException {
        this(new URI(ProxyHelper.PROXY_CLASS_PREFIX + ProxyHelper.getProxyClassCount() + JAVA_SOURCE_FILE.extension), JAVA_SOURCE_FILE, type);
    }

    private CodeFile(URI uri, Kind kind, Class<?> type) {
        super(uri, kind);
        this.src = generateSrc(type);
    }

    /**
     * cglib
     */
    private String generateSrc(Class<?> type) {
        List<String> names = new ArrayList<>();
        names.add("equals");
        names.add("toString");
        //利用反射生成java源代码
        StringBuilder sb = new StringBuilder();
        sb.append("package ").append(ProxyClassLoader.class.getPackage().getName()).append(";\n");
        sb.append("import ").append(MethodInterceptor.class.getName()).append(";\n");
        sb.append("import ").append(type.getName()).append(";\n");
        sb.append("import ").append(MethodProxy.class.getName()).append(";\n");
        sb.append("import java.lang.reflect.*;\n");
        sb.append("public class $Proxy").append(ProxyHelper.getProxyClassCount()).append(" extends ").append(type.getSimpleName()).append(" {\n");
        sb.append("private MethodInterceptor h;\n");
        sb.append("public $Proxy").append(ProxyHelper.getProxyClassCount()).append("(MethodInterceptor h){\nthis.h=h;\n}\n");
        for (Method method : type.getMethods()) {
            String modifiers = Modifier.toString(method.getModifiers());
            Class<?> returnType = method.getReturnType();
            String returnTypeName = returnType.getName();
            if (!names.contains(method.getName()) && !modifiers.contains("final") && !modifiers.contains("native")) {
                sb.append("@Override\n");
                sb.append(modifiers.replace(" abstract", "")).append(" ");
                sb.append(returnTypeName).append(" ");
                sb.append(method.getName()).append("(");
                for (Parameter parameter : method.getParameters()) {
                    Class<?> paramType = parameter.getType();
                    String paramTypeName = paramType.getName();
                    sb.append(paramTypeName).append(" ").append(parameter.getName());
                    sb.append(",");
                }
                deleteRedundantChar(sb, "(");
                sb.append(")").append(" {\n");

                sb.append("Object $result = null;\n");
                sb.append("try{\n");
                sb.append("Method m = ").append(method.getDeclaringClass().getSimpleName()).append(".class.getMethod(\"").append(method.getName()).append("\",");
                for (Parameter parameter : method.getParameters()) {
                    sb.append(parameter.getType().getName()).append(".class");
                    sb.append(",");
                }
                deleteRedundantChar(sb, "(");
                sb.append(");\n");

                sb.append("$result = this.h.intercept(this,m,new Object[]{");
                for (Parameter parameter : method.getParameters()) {
                    sb.append(parameter.getName());
                    sb.append(",");
                }
                deleteRedundantChar(sb, "{");
                sb.append("},new MethodProxy(m));\n");

                sb.append("}catch (Throwable t) {\nt.printStackTrace();\n}\n");
                if (!"java.lang.Void".equals(returnTypeName) && !"void".equals(returnTypeName)) {
                    sb.append("return (").append(returnTypeName).append(")$result;\n");
                }
                sb.append("}\n");
            }
        }

        sb.append("}\n");
        System.out.println(sb.toString());
        return sb.toString();
    }

    private void deleteRedundantChar(StringBuilder sb, String str) {
        //这里可能会多出1个","
        if (sb.length() - 1 != sb.lastIndexOf(str)) {
            sb.deleteCharAt(sb.length() - 1);
        }
    }

    @Override
    public CharSequence getCharContent(boolean ignoreEncodingErrors) {
        return this.src;
    }
}