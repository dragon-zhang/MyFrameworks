package Ybatis.pojo;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SQL {
    private String origin;
    private String type;
    private Fields fields;
    private Values values;
    private String table;
    private Where where;
    private String limit;

    public SQL(String type, Fields fields, Values values, String table, Where where, String limit) {
        this.type = type;
        this.fields = fields;
        this.values = values;
        this.table = table;
        this.where = where;
        this.limit = limit;
    }
}
