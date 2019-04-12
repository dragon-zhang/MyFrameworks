package MyDynamicProxy.loader;

import MyDynamicProxy.source.JavaClassFile;

/**
 * @author SuccessZhang
 * 自定义类加载器
 */
public class MyClassLoader extends ClassLoader {

    private JavaClassFile stringObject;

    public MyClassLoader(JavaClassFile stringObject) {
        this.stringObject = stringObject;
    }

    @Override
    protected Class<?> findClass(String name) {
        byte[] bytes = this.stringObject.getBytes();
        //定义的class和MyClassLoader在相同的包下
        return defineClass(MyClassLoader.class.getPackage().getName() + "." + name, bytes, 0, bytes.length);
    }
}