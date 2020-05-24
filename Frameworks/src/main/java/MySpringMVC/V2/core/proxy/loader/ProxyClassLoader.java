package MySpringMVC.V2.core.proxy.loader;

import MySpringMVC.V2.core.proxy.source.ClassFile;

/**
 * 自定义类加载器
 *
 * @author SuccessZhang
 * @date 2020/04/10
 */
public class ProxyClassLoader extends ClassLoader {

    private ClassFile classFile;

    public ProxyClassLoader(ClassFile classFile) {
        this.classFile = classFile;
    }

    @Override
    protected Class<?> findClass(String name) {
        byte[] bytes = this.classFile.getBytes();
        //定义的class和MyClassLoader在相同的包下
        return defineClass(ProxyClassLoader.class.getPackage().getName() + "." + name, bytes, 0, bytes.length);
    }
}