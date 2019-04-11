package MyDynamicProxy;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import java.lang.reflect.Constructor;
import java.net.URI;
import java.util.Collections;

/**
 * @author SuccessZhang
 */
public class MyProxy {

    @SuppressWarnings("unchecked")
    public static Object newProxyInstance(MyClassLoader loader,
                                          Class<?>[] interfaces,
                                          MyInvocationHandler h) {
        try {
            //1.生成代码
            String src = generateSrc(interfaces);
            //2.从内存中创建class

            JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
            StandardJavaFileManager standardFileManager = compiler.getStandardFileManager(null, null, null);
            JavaClassFileManager classJavaFileManager = new JavaClassFileManager(standardFileManager);
            StringSrcCode stringObject = new StringSrcCode(new URI("$Proxy0.java"), JavaFileObject.Kind.SOURCE, src);
            JavaCompiler.CompilationTask task = compiler.getTask(null, classJavaFileManager, null, null, null, Collections.singletonList(stringObject));

            if (task.call()) {
                JavaClassFile javaFileObject = classJavaFileManager.getClassJavaFileObject();
                ClassLoader classLoader = new MyClassLoader(javaFileObject);
                Class proxyClass = classLoader.loadClass("$Proxy0");
                Constructor c = proxyClass.getConstructor(MyInvocationHandler.class);
                return c.newInstance(h);
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String generateSrc(Class<?>[] interfaces) {
        StringBuilder sb = new StringBuilder();
        sb.append("package MyDynamicProxy;\n");
        sb.append("import MyDynamicProxy.test.Service;\n");
        sb.append("import java.lang.reflect.*;\n");
        sb.append("public class $Proxy0 implements ");
        for (Class<?> iInterface : interfaces) {
            String name = iInterface.getName();
            if (name.contains("Service")) {
                sb.append(name);
                sb.append(",");
            }
        }
        //这里会多出1个","
        sb.deleteCharAt(sb.length() - 1);
        sb.append("{\n");
        sb.append("private MyInvocationHandler h;\n");
        sb.append("public $Proxy0(MyInvocationHandler h){\nthis.h=h;\n}\n");
        sb.append("@Override\n");
        sb.append("public Object test() {\n");
        sb.append("Object result = null;\n");
        sb.append("try{\n");
        sb.append("Method m = Service.class.getMethod(\"test\");\n");
        sb.append("result = this.h.invoke(this,m,null);\n");
        sb.append("}catch (Throwable t) {\nt.printStackTrace();\n}\n");
        sb.append("return result;\n");
        sb.append("}\n");
        sb.append("}\n");
        System.out.println(sb.toString());
        return sb.toString();
    }
}
