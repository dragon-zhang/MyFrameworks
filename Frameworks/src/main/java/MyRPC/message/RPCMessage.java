package MyRPC.message;

import lombok.Data;

/**
 * @author SuccessZhang
 */
@Data
public class RPCMessage {
    private String className;
    private String methodName;
    private Object[] params;
}
