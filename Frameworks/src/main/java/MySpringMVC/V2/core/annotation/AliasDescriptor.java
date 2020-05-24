package MySpringMVC.V2.core.annotation;

import MySpringMVC.V2.core.annotation.utils.StringUtils;
import lombok.Data;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * 别名描述器，用于描述{@link AliasFor}细节
 *
 * @author SuccessZhang
 * @date 2020/05/12
 */
@Data
public class AliasDescriptor {
    /**
     * 源注解属性方法
     */
    private final Method sourceAttribute;

    /**
     * 源注解类型
     */
    private final Class<? extends Annotation> sourceAnnotationType;

    /**
     * 别名的注解属性方法
     */
    private final Method aliasAttribute;

    /**
     * 别名的注解类型
     */
    private final Class<? extends Annotation> aliasAnnotationType;

    /**
     * 别名的属性名
     */
    private final String aliasAttributeName;

    public static AliasDescriptor from(Method attribute) {
        AliasFor aliasFor = attribute.getAnnotation(AliasFor.class);
        if (aliasFor == null) {
            return null;
        }
        return new AliasDescriptor(attribute, aliasFor);
    }

    @SuppressWarnings("unchecked")
    private AliasDescriptor(Method sourceAttribute, AliasFor aliasFor) {
        Class<?> declaringClass = sourceAttribute.getDeclaringClass();

        this.sourceAttribute = sourceAttribute;
        this.sourceAnnotationType = (Class<? extends Annotation>) declaringClass;
        this.aliasAnnotationType = aliasFor.annotation();
        this.aliasAttributeName = getAliasAttributeName(aliasFor, sourceAttribute);

        // 源注解属性名（即方法名）
        String sourceAttributeName = sourceAttribute.getName();
        if (this.aliasAnnotationType == this.sourceAnnotationType) {
            String msg = String.format("@%s注解的%s属性方法上的@AliasFor注解中申明的覆盖注解类型不能与该方法声明类一致!",
                    declaringClass.getName(), sourceAttribute.getName());
            throw new RuntimeException(msg);
        }
        try {
            this.aliasAttribute = this.aliasAnnotationType.getDeclaredMethod(this.aliasAttributeName);
        } catch (NoSuchMethodException ex) {
            String msg = String.format(
                    "@%s注解中%s属性方法上@AliasFor注解对应的%s别名属性不存在于%s别名注解类中！",
                    this.sourceAnnotationType.getName(), sourceAttributeName, this.aliasAttributeName,
                    this.aliasAnnotationType.getName());
            throw new RuntimeException(msg, ex);
        }
        // 要覆盖的注解类型必修是源注解的元注解
        if (!sourceAnnotationType.isAnnotationPresent(aliasAnnotationType)) {
            String msg = String.format("'@%s'注解的'%s'属性方法上的@AliasFor所声明的annotation别名注解类型不是该注解的元注解!",
                    sourceAnnotationType.getName(), sourceAttributeName);
            throw new IllegalArgumentException(msg);
        }
        // 判断别名属性方法与源属性方法返回值是否一致
        if (sourceAttribute.getReturnType() != aliasAttribute.getReturnType()) {
            String msg = String.format("'@%s'注解的'%s'属性方法类型与该方法上@AliasFor所声明的'@%s'注解类型中的'%s'属性方法类型不匹配!",
                    sourceAnnotationType.getName(), sourceAttributeName, aliasAnnotationType.getName(), aliasAttributeName);
            throw new IllegalArgumentException(msg);
        }
    }

    /**
     * 获取属性方法上的被覆盖的属性名
     *
     * @param aliasFor        别名注解
     * @param sourceAttribute 注解属性方法
     * @return 如果 {@link AliasFor} 注解的attribute和value属性值都为空，则反回sourceAttribute的方法名
     */
    private String getAliasAttributeName(AliasFor aliasFor, Method sourceAttribute) {
        String attribute = aliasFor.attribute();
        String value = aliasFor.value();
        boolean hasAttribute = !StringUtils.isEmpty(attribute);
        boolean hasValue = !StringUtils.isEmpty(value);
        if (hasAttribute && hasValue) {
            String msg = String.format("%s注解中的%s属性方法上的@AliasFor注解的value属性和attribute不能同时存在！",
                    sourceAttribute.getDeclaringClass().getName(), sourceAttribute.getName());
            throw new RuntimeException(msg);
        }
        attribute = hasAttribute ? attribute : value;
        return !StringUtils.isEmpty(attribute) ? attribute : sourceAttribute.getName();
    }
}