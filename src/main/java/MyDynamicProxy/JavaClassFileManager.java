package MyDynamicProxy;

import javax.tools.FileObject;
import javax.tools.ForwardingJavaFileManager;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import java.io.IOException;

/**
 * @author SuccessZhang
 */
public class JavaClassFileManager extends ForwardingJavaFileManager {

    private JavaClassFile classJavaFileObject;

    public JavaClassFileManager(JavaFileManager fileManager) {
        super(fileManager);
    }

    public JavaClassFile getClassJavaFileObject() {
        return classJavaFileObject;
    }

    @Override
    public JavaFileObject getJavaFileForOutput(JavaFileManager.Location location, String className, JavaFileObject.Kind kind, FileObject sibling) throws IOException {
        return classJavaFileObject = new JavaClassFile(className, kind);
    }

}