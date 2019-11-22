package MyDynamicProxy.source;

import MyDynamicProxy.MyInvocationHandler;
import MyDynamicProxy.loader.MyClassLoader;
import MyDynamicProxy.test.TestService;
import MySpringMVC.V2.aop.AOPMethods;

import javax.tools.SimpleJavaFileObject;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * @author SuccessZhang
 * 存储java源代码
 */
public class StringSrcCode extends SimpleJavaFileObject {

    private String src;

    public StringSrcCode(URI uri, Kind kind, Class<?> type) {
        super(uri, kind);
        this.src = generateSrc(type);
    }

    private String generateSrc(Class<?> type) {
        List<String> names = getAOPMethodNames();
        names.add("equals");
        names.add("toString");
        //利用反射生成java源代码
        StringBuilder sb = new StringBuilder();
        sb.append("package ").append(MyClassLoader.class.getPackage().getName()).append(";\n");
        sb.append("import ").append(MyInvocationHandler.class.getName()).append(";\n");
        sb.append("import ").append(type.getName()).append(";\n");
        sb.append("import java.lang.reflect.*;\n");
        sb.append("public class $Proxy0 extends ").append(type.getSimpleName()).append(" {\n");
        sb.append("private MyInvocationHandler h;\n");
        sb.append("public $Proxy0(MyInvocationHandler h){\nthis.h=h;\n}\n");
        for (Method method : type.getMethods()) {
            String modifiers = Modifier.toString(method.getModifiers());
            if (!names.contains(method.getName()) && !modifiers.contains("final") && !modifiers.contains("native")) {
                sb.append("@Override\n");
                sb.append(modifiers.replace(" abstract", "")).append(" ");
                sb.append(method.getReturnType().getSimpleName()).append(" ");
                sb.append(method.getName()).append("(");
                for (Parameter parameter : method.getParameters()) {
                    sb.append(parameter.getType().getSimpleName()).append(" ").append(parameter.getName());
                    sb.append(",");
                }
                deleteRedundantChar(sb, "(");
                sb.append(")").append(" {\n");

                sb.append("Object result = null;\n");
                sb.append("try{\n");
                sb.append("Method m = ").append(method.getDeclaringClass().getSimpleName()).append(".class.getMethod(\"").append(method.getName()).append("\",");
                for (Parameter parameter : method.getParameters()) {
                    sb.append(parameter.getType().getSimpleName()).append(".class");
                    sb.append(",");
                }
                deleteRedundantChar(sb, "(");
                sb.append(");\n");

                sb.append("result = this.h.invoke(this,m,new Object[]{");
                for (Parameter parameter : method.getParameters()) {
                    sb.append(parameter.getName());
                    sb.append(",");
                }
                deleteRedundantChar(sb, "{");

                sb.append("});\n");
                sb.append("}catch (Throwable t) {\nt.printStackTrace();\n}\n");
                sb.append("return result;\n");
                sb.append("}\n");
            }
        }

        sb.append("}\n");
        System.out.println(sb.toString());
        return sb.toString();
    }

    public List<String> getAOPMethodNames() {
        Method[] aopMethods = AOPMethods.class.getDeclaredMethods();
        List<String> aopMethodNames = new ArrayList<>(aopMethods.length);
        for (Method aopMethod : aopMethods) {
            aopMethodNames.add(aopMethod.getName());
        }
        return aopMethodNames;
    }

    public StringSrcCode(URI uri, Kind kind, Class<?>[] interfaces) {
        super(uri, kind);
        this.src = generateSrc(interfaces);
    }

    private String generateSrc(Class<?>[] interfaces) {
        //利用反射生成java源代码
        StringBuilder sb = new StringBuilder();
        sb.append("package ").append(MyClassLoader.class.getPackage().getName()).append(";\n");
        sb.append("import ").append(MyInvocationHandler.class.getName()).append(";\n");
        sb.append("import ").append(TestService.class.getName()).append(";\n");
        sb.append("import java.lang.reflect.*;\n");
        sb.append("public class $Proxy0 implements ");
        for (Class<?> iInterface : interfaces) {
            String name = iInterface.getSimpleName();
            if (!name.contains(AOPMethods.class.getSimpleName())) {
                sb.append(name);
                sb.append(",");
            }
        }
        deleteRedundantChar(sb, "");
        sb.append(" {\n");
        sb.append("private MyInvocationHandler h;\n");
        sb.append("public $Proxy0(MyInvocationHandler h){\nthis.h=h;\n}\n");
        for (Method method : TestService.class.getMethods()) {
            sb.append("@Override\n");
            sb.append(Modifier.toString(method.getModifiers()).replace(" abstract", "")).append(" ");
            sb.append(method.getReturnType().getSimpleName()).append(" ");
            sb.append(method.getName()).append("(");
            for (Parameter parameter : method.getParameters()) {
                sb.append(parameter.getType().getSimpleName()).append(" ").append(parameter.getName());
                sb.append(",");
            }
            deleteRedundantChar(sb, "(");
            sb.append(")").append(" {\n");

            sb.append("Object result = null;\n");
            sb.append("try{\n");
            sb.append("Method m = ").append(method.getDeclaringClass().getSimpleName()).append(".class.getMethod(\"").append(method.getName()).append("\",");
            for (Parameter parameter : method.getParameters()) {
                sb.append(parameter.getType().getSimpleName()).append(".class");
                sb.append(",");
            }
            deleteRedundantChar(sb, "(");
            sb.append(");\n");

            sb.append("result = this.h.invoke(this,m,new Object[]{");
            for (Parameter parameter : method.getParameters()) {
                sb.append(parameter.getName());
                sb.append(",");
            }
            deleteRedundantChar(sb, "{");

            sb.append("});\n");
            sb.append("}catch (Throwable t) {\nt.printStackTrace();\n}\n");
            sb.append("return result;\n");
            sb.append("}\n");
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