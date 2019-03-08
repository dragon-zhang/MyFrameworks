package MyTomcat.http;

import lombok.Data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * @author SuccessZhang
 */
@Data
public class MyRequest {

    /**
     * post、get、put
     */
    private String methodType;

    private String url;

    private String protocol;

    public MyRequest(InputStream inputStream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
        String[] strings = reader.readLine().split(" ");
        this.methodType = strings[0];
        this.url = strings[1];
        this.protocol = strings[2];
    }
}
