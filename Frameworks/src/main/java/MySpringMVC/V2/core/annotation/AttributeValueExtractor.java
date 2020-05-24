package MySpringMVC.V2.core.annotation;

import java.lang.annotation.Annotation;

/**
 * 属性值获取器
 *
 * @author SuccessZhang
 * @date 2020/05/12
 */
public interface AttributeValueExtractor {

    /**
     * 根据属性名获取对应的属性值
     *
     * @param attribute 属性名（注解方法名）
     * @return 属性值
     */
    Object getAttributeValue(String attribute);

    /**
     * 获取注解
     *
     * @return 目标注解
     */
    Annotation getAnnotation();
}
