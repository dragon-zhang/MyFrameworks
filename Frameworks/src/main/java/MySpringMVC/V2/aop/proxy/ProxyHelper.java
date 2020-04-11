package MySpringMVC.V2.aop.proxy;

import MySpringMVC.V2.aop.proxy.cglib.MethodInterceptor;
import MySpringMVC.V2.aop.proxy.jdk.InvocationHandler;
import MySpringMVC.V2.aop.proxy.loader.ProxyClassLoader;
import MySpringMVC.V2.aop.proxy.manager.ClassFileManager;
import MySpringMVC.V2.aop.proxy.source.ClassFile;
import MySpringMVC.V2.aop.proxy.source.CodeFile;

import javax.tools.JavaCompiler;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author SuccessZhang
 * @date 2020/04/10
 */
public class ProxyHelper {

    public static final String PROXY_CLASS_PREFIX = "$Proxy";

    private static int proxyClassCount = 0;

    private static final Map<Class<?>, Object> CLASS_INSTANCE_CACHE = new HashMap<>(16);

    public static synchronized Object newProxyInstanceByJdk(CodeFile codeSource, InvocationHandler h) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        try {
            ClassFileManager classFileManager = new ClassFileManager();
            //4.编译java文件，从内存中生成（非写入）class文件
            JavaCompiler.CompilationTask task = classFileManager.getTask(codeSource);
            if (task.call()) {
                //5.从内存中读取class文件
                ClassFile javaFileObject = classFileManager.getClassFile();
                //6.装载class文件
                ClassLoader classLoader = new ProxyClassLoader(javaFileObject);
                Class<?> proxyClass = classLoader.loadClass(PROXY_CLASS_PREFIX + proxyClassCount);
                //7.创建代理对象实例
                Constructor<?> constructor = proxyClass.getConstructor(InvocationHandler.class);
                return constructor.newInstance(h);
            }
        } finally {
            proxyClassCount++;
        }
        return null;
    }

    public static synchronized Object newProxyInstanceByCglib(CodeFile codeSource, MethodInterceptor callback) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        try {
            ClassFileManager classFileManager = new ClassFileManager();
            //4.编译java文件，从内存中生成（非写入）class文件
            JavaCompiler.CompilationTask task = classFileManager.getTask(codeSource);
            if (task.call()) {
                //5.从内存中读取class文件
                ClassFile javaFileObject = classFileManager.getClassFile();
                //6.装载class文件
                ClassLoader classLoader = new ProxyClassLoader(javaFileObject);
                Class<?> proxyClass = classLoader.loadClass(PROXY_CLASS_PREFIX + proxyClassCount);
                //7.创建代理对象实例
                Constructor<?> constructor = proxyClass.getConstructor(MethodInterceptor.class);
                return constructor.newInstance(callback);
            }
        } finally {
            proxyClassCount++;
        }
        return null;
    }

    public static int getProxyClassCount() {
        return proxyClassCount;
    }

    public static Object getClassInstance(Class<?> superClass) {
        return CLASS_INSTANCE_CACHE.get(superClass);
    }

    public static void putClassInstance(Class<?> superClass, Object classInstance) {
        CLASS_INSTANCE_CACHE.put(superClass, classInstance);
    }
}
