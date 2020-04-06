package MySpringMVC.V2.webmvc.servlet;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class ModelAndView {

    private String viewName;

    private Map<String, Object> model;

    public ModelAndView(String viewName) {
        this.viewName = viewName;
    }

    public ModelAndView(Object result) {
        this.model = new HashMap<>(1);
        this.model.put("data", JSON.toJSONString(result, SerializerFeature.WriteMapNullValue));
        this.viewName = "200";
    }

    public ModelAndView(String viewName, Map<String, Object> model) {
        this.viewName = viewName;
        this.model = model;
    }
}
