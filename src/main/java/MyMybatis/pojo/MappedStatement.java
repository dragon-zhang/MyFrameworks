package MyMybatis.pojo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author SuccessZhang
 */
@Data
public class MappedStatement {

    private String namespace;
    private String id;
    private String resultType;
    private String sql;
    private List<String> paramNames;

    public MappedStatement(String namespace, String id, String resultType, String sql) {
        this.namespace = namespace;
        this.id = id;
        this.resultType = resultType;
        this.sql = sql;
        paramNames = new ArrayList<>();
    }
}
