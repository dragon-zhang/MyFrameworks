package MyDubbo.bean;

import lombok.Data;

import java.util.List;

/**
 * @author SuccessZhang
 */
@Data
public class Message {
    private String className;
    private String methodName;
    private List<Object> params;
    private Class<?>[] paramTypes;
}
