package MySpringMVC.V2.core.proxy.manager;

import MySpringMVC.V2.core.proxy.source.ClassFile;
import MySpringMVC.V2.core.proxy.source.CodeFile;
import lombok.Getter;

import javax.tools.FileObject;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.Iterator;
import java.util.Set;

/**
 * 管理class文件
 *
 * @author SuccessZhang
 * @date 2020/04/11
 */
public class ClassFileManager implements JavaFileManager {

    private final JavaCompiler compiler;

    private final StandardJavaFileManager fileManager;

    @Getter
    private ClassFile classFile;

    public ClassFileManager() {
        /*1.获取编译器*/
        compiler = ToolProvider.getSystemJavaCompiler();
        /*2.获取class文件管理器*/
        this.fileManager = compiler.getStandardFileManager(null, null, Charset.forName("UTF-8"));
    }

    public JavaCompiler.CompilationTask getTask(CodeFile codeSource) {
        return compiler.getTask(null, this, null, null, null, Collections.singletonList(codeSource));
    }

    @Override
    public ClassLoader getClassLoader(Location location) {
        return this.fileManager.getClassLoader(location);
    }

    @Override
    public Iterable<JavaFileObject> list(Location location, String packageName, Set<JavaFileObject.Kind> kinds, boolean recurse) throws IOException {
        return this.fileManager.list(location, packageName, kinds, recurse);
    }

    @Override
    public String inferBinaryName(Location location, JavaFileObject file) {
        return this.fileManager.inferBinaryName(location, file);
    }

    @Override
    public boolean isSameFile(FileObject a, FileObject b) {
        return this.fileManager.isSameFile(a, b);
    }

    @Override
    public boolean handleOption(String current, Iterator<String> remaining) {
        return this.fileManager.handleOption(current, remaining);
    }

    @Override
    public boolean hasLocation(Location location) {
        return this.fileManager.hasLocation(location);
    }

    @Override
    public JavaFileObject getJavaFileForInput(Location location, String className, JavaFileObject.Kind kind) throws IOException {
        return this.fileManager.getJavaFileForInput(location, className, kind);
    }

    @Override
    public JavaFileObject getJavaFileForOutput(Location location, String className, JavaFileObject.Kind kind, FileObject sibling) {
        this.classFile = new ClassFile(className, kind);
        return this.classFile;
    }

    @Override
    public FileObject getFileForInput(Location location, String packageName, String relativeName) throws IOException {
        return this.fileManager.getFileForInput(location, packageName, relativeName);
    }

    @Override
    public FileObject getFileForOutput(Location location, String packageName, String relativeName, FileObject sibling) throws IOException {
        return this.fileManager.getFileForOutput(location, packageName, relativeName, sibling);
    }

    @Override
    public void flush() throws IOException {
        this.fileManager.flush();
    }

    @Override
    public void close() throws IOException {
        this.fileManager.close();
    }

    @Override
    public int isSupportedOption(String option) {
        return 0;
    }
}