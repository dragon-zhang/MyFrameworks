package MySpringMVC.V2.aop.proxy.source;

import javax.tools.SimpleJavaFileObject;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.net.URI;

/**
 * 存储class源文件
 *
 * @author SuccessZhang
 * @date 2020/04/11
 */
public class ClassFile extends SimpleJavaFileObject {

    private ByteArrayOutputStream outputStream;

    public ClassFile(String className, Kind kind) {
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