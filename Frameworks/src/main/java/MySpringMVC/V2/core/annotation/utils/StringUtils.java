package MySpringMVC.V2.core.annotation.utils;

/**
 * String工具类
 *
 * @author hml
 * @date 2018/6/14 下午3:01
 */
public class StringUtils {

    /**
     * 判断字符串是否为空且长度为0
     *
     * @param str 字符串
     * @return 为空或0返回true
     */
    public static boolean isEmpty(String str) {
        return str == null || str.trim().length() == 0;
    }

}
