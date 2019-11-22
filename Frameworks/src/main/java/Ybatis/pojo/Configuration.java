package Ybatis.pojo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

/**
 * @author SuccessZhang
 */
@Data
@NoArgsConstructor
public class Configuration {

    private Jdbc jdbc;
    private HashMap<String, Map<String, Method>> mappers;

}
