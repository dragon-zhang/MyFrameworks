package MyDynamicProxy.manager;

import MyDynamicProxy.source.JavaClassFile;

import javax.tools.FileObject;
import javax.tools.ForwardingJavaFileManager;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;

/**
 * @author SuccessZhang
 * 管理class文件
 */
public class JavaClassFileManager extends ForwardingJavaFileManager {

    private JavaClassFile classJavaFileObject;

    @SuppressWarnings("unchecked")
    public JavaClassFileManager(JavaFileManager fileManager) {
        super(fileManager);
    }

    public JavaClassFile getClassJavaFileObject() {
        return classJavaFileObject;
    }

    @Override
    public JavaFileObject getJavaFileForOutput(JavaFileManager.Location location,
                                               String className,
                                               JavaFileObject.Kind kind,
                                               FileObject sibling) {
        return classJavaFileObject = new JavaClassFile(className, kind);
    }

}