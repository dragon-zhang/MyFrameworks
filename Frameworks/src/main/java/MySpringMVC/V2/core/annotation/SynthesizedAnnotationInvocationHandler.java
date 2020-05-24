package MySpringMVC.V2.core.annotation;

import MySpringMVC.V2.core.annotation.utils.ReflectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Objects;

/**
 * 组合注解代理处理器
 *
 * @author SuccessZhang
 * @date 2020/05/12
 */
public class SynthesizedAnnotationInvocationHandler implements InvocationHandler {

    private static final String ANNOTATION_TYPE_METHOD_NAME = "annotationType";

    /**
     * 属性值提取器
     */
    private final AttributeValueExtractor attributeValueExtractor;

    public SynthesizedAnnotationInvocationHandler(AttributeValueExtractor valueExtractor) {
        this.attributeValueExtractor = valueExtractor;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) {
        if (ReflectionUtils.isEqualsMethod(method)) {
            return annotationEquals(args[0]);
        }
        if (ReflectionUtils.isHashCodeMethod(method)) {
            return annotationHashCode();
        }
        if (ReflectionUtils.isToStringMethod(method)) {
            return annotationToString();
        }
        if (ANNOTATION_TYPE_METHOD_NAME.equals(method.getName()) && method.getParameterCount() == 0) {
            return annotationType();
        }
        return attributeValueExtractor.getAttributeValue(method.getName());
    }

    private Object getAttributeValue(Method attributeMethod) {
        return attributeValueExtractor.getAttributeValue(attributeMethod.getName());
    }

    private Class<? extends Annotation> annotationType() {
        return attributeValueExtractor.getAnnotation().annotationType();
    }

    /**
     * 代理组合注解equals方法 {@link Annotation#equals(Object)}
     */
    private boolean annotationEquals(Object other) {
        if (this == other) {
            return true;
        }
        if (!annotationType().isInstance(other)) {
            return false;
        }
        for (Method attributeMethod : AnnotationUtils.getAttributeMethods(annotationType())) {
            Object thisValue = getAttributeValue(attributeMethod);
            Object otherValue = ReflectionUtils.invokeMethod(attributeMethod, other);
            if (!Objects.equals(thisValue, otherValue)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 代理组合注解hashCode方法 {@link Annotation#hashCode()}
     */
    private int annotationHashCode() {
        int result = 0;
        for (Method attributeMethod : AnnotationUtils.getAttributeMethods(annotationType())) {
            Object value = getAttributeValue(attributeMethod);
            int hashCode;
            if (value.getClass().isArray()) {
                hashCode = hashCodeForArray(value);
            } else {
                hashCode = value.hashCode();
            }
            result += (127 * attributeMethod.getName().hashCode()) ^ hashCode;
        }
        return result;
    }

    private int hashCodeForArray(Object array) {
        if (array instanceof boolean[]) {
            return Arrays.hashCode((boolean[]) array);
        }
        if (array instanceof byte[]) {
            return Arrays.hashCode((byte[]) array);
        }
        if (array instanceof char[]) {
            return Arrays.hashCode((char[]) array);
        }
        if (array instanceof double[]) {
            return Arrays.hashCode((double[]) array);
        }
        if (array instanceof float[]) {
            return Arrays.hashCode((float[]) array);
        }
        if (array instanceof int[]) {
            return Arrays.hashCode((int[]) array);
        }
        if (array instanceof long[]) {
            return Arrays.hashCode((long[]) array);
        }
        if (array instanceof short[]) {
            return Arrays.hashCode((short[]) array);
        }
        // else
        return Arrays.hashCode((Object[]) array);
    }

    /**
     * 代理组合注解toString方法
     */
    private String annotationToString() {
        StringBuilder sb = new StringBuilder();
        sb.append("@").append(annotationType().getName()).append("(");
        Iterator<Method> iterator = AnnotationUtils.getAttributeMethods(annotationType()).iterator();
        while (iterator.hasNext()) {
            Method attributeMethod = iterator.next();
            sb.append(attributeMethod.getName());
            sb.append('=');
            sb.append(getAttributeValue(attributeMethod));
            sb.append(iterator.hasNext() ? ", " : "");
        }
        return sb.append(")").toString();
    }

}