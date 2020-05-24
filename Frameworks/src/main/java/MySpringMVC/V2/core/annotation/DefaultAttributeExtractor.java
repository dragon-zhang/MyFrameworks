package MySpringMVC.V2.core.annotation;

import MySpringMVC.V2.core.annotation.utils.ReflectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 默认属性值提取器（将属性值和属性名存于map，并从中获取对应的属性值）
 *
 * @author SuccessZhang
 * @date 2020/05/12
 */
public class DefaultAttributeExtractor implements AttributeValueExtractor {

    /**
     * 目标注解
     */
    private Annotation annotation;

    /**
     * 注解属性 key:属性名（即注解中的方法名） value:属性值
     */
    private Map<String, Object> attributes;

    private DefaultAttributeExtractor(Annotation annotation, Map<String, Object> attributes) {
        this.annotation = annotation;
        this.attributes = attributes;
    }

    /**
     * 生成默认的属性值提取器
     *
     * @param target  目标注解
     * @param visited 目标注解的访问路径
     * @return 默认属性值提取器
     */
    public static DefaultAttributeExtractor from(AnnotatedElement element, Annotation target, Deque<Annotation> visited) {
        Class<? extends Annotation> targetType = target.annotationType();
        Map<String, Object> attributes = new LinkedHashMap<>(8);
        for (Annotation annotation : visited) {
            List<AliasDescriptor> aliasDescriptors = getAliasDescriptors(annotation);
            if (aliasDescriptors.isEmpty()) {
                continue;
            }
            // 遍历该注解上的所有别名描述器，并判断目标注解的属性是否有被其他注解覆盖
            for (AliasDescriptor descriptor : aliasDescriptors) {
                if (descriptor.getAliasAnnotationType() == targetType) {
                    String targetAttributeName = descriptor.getAliasAttributeName();
                    if (!attributes.containsKey(targetAttributeName)) {
                        attributes.put(targetAttributeName,
                                ReflectionUtils.invokeMethod(descriptor.getSourceAttribute(),
                                        AnnotationUtils.getMergedAnnotation(element, descriptor.getSourceAnnotationType()))
                        );
                    }
                }
            }
        }
        // 判断哪些属性没有被覆盖，没覆盖则补上对应的属性值
        for (Method targetAttribute : AnnotationUtils.getAttributeMethods(targetType)) {
            String attributeName = targetAttribute.getName();
            if (!attributes.containsKey(attributeName)) {
                attributes.put(attributeName, ReflectionUtils.invokeMethod(targetAttribute, target));
            }
        }
        return new DefaultAttributeExtractor(target, attributes);
    }

    /**
     * 获取指定注解上的所有属性方法含有的别名属性描述
     */
    private static List<AliasDescriptor> getAliasDescriptors(Annotation annotation) {
        List<AliasDescriptor> descriptors = null;
        for (Method attribute : AnnotationUtils.getAttributeMethods(annotation.annotationType())) {
            AliasDescriptor des = AliasDescriptor.from(attribute);
            if (des != null) {
                if (descriptors == null) {
                    descriptors = new ArrayList<>(8);
                }
                descriptors.add(des);
            }
        }
        return Optional.ofNullable(descriptors).orElse(Collections.emptyList());
    }

    @Override
    public Object getAttributeValue(String attribute) {
        return this.attributes.get(attribute);
    }

    @Override
    public Annotation getAnnotation() {
        return this.annotation;
    }
}