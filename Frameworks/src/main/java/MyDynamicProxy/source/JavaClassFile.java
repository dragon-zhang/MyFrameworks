package MyDynamicProxy.source;

import javax.tools.SimpleJavaFileObject;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.net.URI;

/**
 * @author SuccessZhang
 * 存储class源文件
 */
public class JavaClassFile extends SimpleJavaFileObject {

    private ByteArrayOutputStream outputStream;

    public JavaClassFile(String className, Kind kind) {
        super(URI.create(className + kind.extension), kind);
        this.outputStream = new ByteArrayOutputStream();
    }

    @Override
    public OutputStream openOutputStream() {
        return this.outputStream;
    }

    public byte[] getBytes() {
        return this.outputStream.toByteArray();
    }
}