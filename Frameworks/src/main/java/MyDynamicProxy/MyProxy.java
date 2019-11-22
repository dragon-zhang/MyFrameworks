package MyDynamicProxy;

import MyDynamicProxy.loader.MyClassLoader;
import MyDynamicProxy.manager.JavaClassFileManager;
import MyDynamicProxy.source.JavaClassFile;
import MyDynamicProxy.source.StringSrcCode;

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

    /**
     * java版cglib实现
     */
    @SuppressWarnings("unchecked")
    public static Object jglibNewProxyInstance(Class<?> type,
                                               MyInvocationHandler h) {
        try {
            //1.获取编译器
            JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
            //2.获取class文件管理器
            StandardJavaFileManager standardFileManager = compiler.getStandardFileManager(null, null, null);
            JavaClassFileManager classFileManager = new JavaClassFileManager(standardFileManager);
            //3.生成java源代码文件
            StringSrcCode stringObject = new StringSrcCode(new URI("$Proxy0.java"), JavaFileObject.Kind.SOURCE, type);
            //4.编译java文件，从内存中生成（非写入）class文件
            JavaCompiler.CompilationTask task = compiler.getTask(null, classFileManager, null, null, null, Collections.singletonList(stringObject));
            if (task.call()) {
                //5.从内存中读取class文件
                JavaClassFile javaFileObject = classFileManager.getClassJavaFileObject();
                //6.装载class文件
                ClassLoader classLoader = new MyClassLoader(javaFileObject);
                Class proxyClass = classLoader.loadClass("$Proxy0");
                //7.创建代理对象实例
                Constructor constructor = proxyClass.getConstructor(MyInvocationHandler.class);
                return constructor.newInstance(h);
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public static Object jdkNewProxyInstance(Class<?>[] interfaces,
                                             MyInvocationHandler h) {
        try {
            //1.获取编译器
            JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
            //2.获取class文件管理器
            StandardJavaFileManager standardFileManager = compiler.getStandardFileManager(null, null, null);
            JavaClassFileManager classFileManager = new JavaClassFileManager(standardFileManager);
            //3.生成java源代码
            StringSrcCode stringObject = new StringSrcCode(new URI("$Proxy0.java"), JavaFileObject.Kind.SOURCE, interfaces);
            //4.编译java文件，非写入
            JavaCompiler.CompilationTask task = compiler.getTask(null, classFileManager, null, null, null, Collections.singletonList(stringObject));
            if (task.call()) {
                //5.从内存中读取class文件
                JavaClassFile javaFileObject = classFileManager.getClassJavaFileObject();
                //6.装载class文件
                ClassLoader classLoader = new MyClassLoader(javaFileObject);
                Class proxyClass = classLoader.loadClass("$Proxy0");
                //7.创建代理对象实例
                Constructor constructor = proxyClass.getConstructor(MyInvocationHandler.class);
                return constructor.newInstance(h);
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
