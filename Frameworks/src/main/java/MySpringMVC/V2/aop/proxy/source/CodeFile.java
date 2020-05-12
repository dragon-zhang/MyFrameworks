package MySpringMVC.V2.aop.proxy.source;

import MySpringMVC.V2.aop.proxy.ProxyHelper;
import MySpringMVC.V2.aop.proxy.cglib.FastClass;
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
        Method[] methods = type.getMethods();
        //利用反射生成java源代码
        StringBuilder sb = new StringBuilder();
        sb.append("package ").append(ProxyClassLoader.class.getPackage().getName()).append(";\n");
        sb.append("import ").append(FastClass.class.getName()).append(";\n");
        sb.append("import ").append(MethodInterceptor.class.getName()).append(";\n");
        sb.append("import ").append(type.getName()).append(";\n");
        sb.append("import ").append(MethodProxy.class.getName()).append(";\n");
        sb.append("import java.lang.reflect.*;\n");
        sb.append("public class $Proxy").append(ProxyHelper.getProxyClassCount()).append(" extends ").append(type.getSimpleName()).append(" implements ").append(FastClass.class.getSimpleName()).append(" {\n");
        sb.append("private MethodInterceptor h;\n");
        sb.append("public $Proxy").append(ProxyHelper.getProxyClassCount()).append("(MethodInterceptor h){\nthis.h=h;\n}\n");
        for (Method method : methods) {
            String modifiers = Modifier.toString(method.getModifiers());
            String returnTypeName = method.getReturnType().getName();
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
                sb.append("Method m = ").append(type.getSimpleName()).append(".class.getMethod(\"").append(method.getName()).append("\",");
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
                sb.append("},new MethodProxy(this,m));\n");

                sb.append("}catch (Throwable t) {\nt.printStackTrace();\n}\n");
                if (!"java.lang.Void".equals(returnTypeName) && !"void".equals(returnTypeName)) {
                    sb.append("return (").append(returnTypeName).append(")$result;\n");
                }
                sb.append("}\n");
            }
        }

        implementsFastClass(type, names, methods, sb);

        sb.append("}\n");
        System.out.println(sb.toString());
        return sb.toString();
    }

    /**
     * 实现{@link MySpringMVC.V2.aop.proxy.cglib.FastClass}接口。
     */
    private void implementsFastClass(Class<?> type, List<String> names, Method[] methods, StringBuilder sb) {
        //实现getIndex
        sb.append("@Override\n");
        sb.append("public int getIndex(String name, Class[] parameterTypes) {\n");
        sb.append("StringBuilder sb = new StringBuilder(name);\n");
        sb.append("sb.append(\"(\");\n");
        sb.append("for (Class parameterType : parameterTypes) {\n");
        sb.append("sb.append(parameterType.getName()).append(\",\");\n");
        sb.append("}\n");
        sb.append("if (sb.length() - 1 != sb.lastIndexOf(\"(\")) {\n");
        sb.append("sb.deleteCharAt(sb.length() - 1);\n");
        sb.append("}\n");
        sb.append("sb.append(\")\");\n");
        sb.append("switch (sb.toString().hashCode()) {\n");
        for (int i = 0; i < methods.length; i++) {
            Method method = methods[i];
            String modifiers = Modifier.toString(method.getModifiers());
            if (!names.contains(method.getName()) && !modifiers.contains("final") && !modifiers.contains("native")) {
                sb.append("case ").append(getHashCode(method.getName(), method.getParameterTypes())).append(":return ").append(i).append(";\n");
            }
        }
        sb.append("default:break;\n");
        sb.append("}\n");
        sb.append("return -1;\n");
        sb.append("}\n");

        //实现invoke
        sb.append("@Override\n");
        sb.append("public Object invoke(int index, Object obj, Object[] args) throws InvocationTargetException {\n");
        sb.append(type.getSimpleName()).append(" object = (").append(type.getSimpleName()).append(") obj;\n");
        sb.append("switch (index) {\n");
        for (int i = 0; i < methods.length; i++) {
            Method method = methods[i];
            String modifiers = Modifier.toString(method.getModifiers());
            if (!names.contains(method.getName()) && !modifiers.contains("final") && !modifiers.contains("native")) {
                sb.append("case ").append(i).append(":");
                String returnTypeName = method.getReturnType().getName();
                if (!"java.lang.Void".equals(returnTypeName) && !"void".equals(returnTypeName)) {
                    sb.append("return ");
                }
                sb.append("object.").append(method.getName()).append("(");
                Class<?>[] parameterTypes = method.getParameterTypes();
                for (int j = 0; j < parameterTypes.length; j++) {
                    sb.append("(").append(parameterTypes[j].getName()).append(")args[").append(j).append("]").append(",");
                }
                deleteRedundantChar(sb, "(");
                sb.append(");\n");
                if ("java.lang.Void".equals(returnTypeName) || "void".equals(returnTypeName)) {
                    sb.append("break;\n");
                }
            }
        }
        sb.append("default:break;\n");
        sb.append("}\n");
        sb.append("return null;\n");
        sb.append("}\n");
    }

    private int getHashCode(String name, Class[] parameterTypes) {
        StringBuilder sb = new StringBuilder(name);
        sb.append("(");
        for (Class parameterType : parameterTypes) {
            sb.append(parameterType.getName()).append(",");
        }
        deleteRedundantChar(sb, "(");
        sb.append(")");
        return sb.toString().hashCode();
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