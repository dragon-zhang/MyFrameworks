package Ybatis.pojo;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author SuccessZhang
 */
@Data
@NoArgsConstructor
public class Jdbc {
    private String driver;
    private String url;
    private String username;
    private String password;
}
