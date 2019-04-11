package MyDynamicProxy;

/**
 * @author SuccessZhang
 */
public class MyClassLoader extends ClassLoader {

    private JavaClassFile stringObject;

    public MyClassLoader(JavaClassFile stringObject) {
        this.stringObject = stringObject;
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        byte[] bytes = this.stringObject.getBytes();
        return defineClass(MyClassLoader.class.getPackage().getName() + "." + name, bytes, 0, bytes.length);
    }
}