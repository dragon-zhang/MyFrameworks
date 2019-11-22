package MyTomcat.http;

import lombok.Data;

import java.io.OutputStream;
import java.nio.channels.SocketChannel;

/**
 * @author SuccessZhang
 */
@Data
public class MyResponse {

    private OutputStream outputStream;

    private StringBuilder header;

    private SocketChannel channel;

    public MyResponse(OutputStream outputStream) {
        header = new StringBuilder();
        this.outputStream = outputStream;
    }

    public MyResponse(SocketChannel channel) {
        header = new StringBuilder();
        this.channel = channel;
    }

    public byte[] response(String protocol, String message) {
        header.append(protocol).append(" 200 \r\n")
                .append("Context-Type: text/html \r\n")
                .append("\r\n");
        header.append(message);
        return header.toString().getBytes();
    }
}
