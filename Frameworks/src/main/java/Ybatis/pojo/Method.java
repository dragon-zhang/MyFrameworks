package Ybatis.pojo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author SuccessZhang
 */
@Data
public class Method {

    private String resultType;
    private SQL sql;
    private List<String> paramNames;
    private Set<String> paramNotNull;

    public Method() {
        this.paramNames = new ArrayList<>();
        this.paramNotNull = new HashSet<>();
    }

    public static Method changeToMethod(Object linkedHashMap) {
        return JSONObject.parseObject(JSON.toJSONString(linkedHashMap), Method.class);
    }
}
