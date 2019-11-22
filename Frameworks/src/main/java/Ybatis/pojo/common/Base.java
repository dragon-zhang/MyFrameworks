package Ybatis.pojo.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author SuccessZhang
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Base {
    private List<String> suppose;
    private List<String> sentence;
}
