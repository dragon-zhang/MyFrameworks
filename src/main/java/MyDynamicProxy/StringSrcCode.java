package MyDynamicProxy;

import javax.tools.JavaFileObject;
import javax.tools.SimpleJavaFileObject;
import java.io.IOException;
import java.net.URI;

/**
 * @author SuccessZhang
 * 存储源文件
 */
public class StringSrcCode extends SimpleJavaFileObject {

    private String content;

    public StringSrcCode(URI uri, JavaFileObject.Kind kind, String content) {
        super(uri, kind);
        this.content = content;
    }

    @Override
    public CharSequence getCharContent(boolean ignoreEncodingErrors) throws IOException {
        return this.content;
    }
}