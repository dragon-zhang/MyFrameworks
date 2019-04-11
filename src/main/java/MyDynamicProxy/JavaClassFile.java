package MyDynamicProxy;

import javax.tools.SimpleJavaFileObject;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;

/**
 * @author SuccessZhang
 */
public class JavaClassFile extends SimpleJavaFileObject {

    private ByteArrayOutputStream outputStream;

    public JavaClassFile(String className, Kind kind) {
        super(URI.create(className + kind.extension), kind);
        this.outputStream = new ByteArrayOutputStream();
    }

    @Override
    public OutputStream openOutputStream() throws IOException {
        return this.outputStream;
    }

    public byte[] getBytes() {
        return this.outputStream.toByteArray();
    }
}