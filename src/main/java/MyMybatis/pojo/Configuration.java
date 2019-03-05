package MyMybatis.pojo;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * @author SuccessZhang
 */
@Data
public class Configuration {

    private String jdbcDriver;
    private String jdbcUrl;
    private String jdbcUserName;
    private String jdbcPassword;
    private Map<String, MappedStatement> mappedStatements;

    public Configuration() {
        this.mappedStatements = new HashMap<>();
    }

}
