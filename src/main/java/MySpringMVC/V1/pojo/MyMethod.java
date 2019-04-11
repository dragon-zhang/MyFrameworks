package MySpringMVC.V1.pojo;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author SuccessZhang
 */
public class MyMethod {

    private Method method;

    /**
     * 这样参数的下标、名称和原参数信息都能获取
     */
    private List<Map<String, Parameter>> params;

    public MyMethod(Method method) {
        this.method = method;
        params = new ArrayList<>();
    }

    public Method getMethod() {
        return method;
    }

    public List<Map<String, Parameter>> getParams() {
        return params;
    }

}
