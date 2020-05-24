package MySpringMVC.V2.core.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2019-04-09 16:27
 */
public final class AnnotationUtils {

    /**
     * 获取指定元素的注解类型（若没有直接注解，则从元素其他注解的元注解上查找）
     *
     * @param annotatedElement     可以包含注解的元素，如Class, Field, Method, Constructor等
     * @param targetAnnotationType 查找的目标注解类型
     * @return 合并后的注解
     */
    @SuppressWarnings("unchecked")
    public static <A extends Annotation> A getMergedAnnotation(AnnotatedElement annotatedElement, Class<A> targetAnnotationType) throws IllegalArgumentException {
        A annotation = annotatedElement.getAnnotation(targetAnnotationType);
        // 如果元素上含有直接注解，则返回
        if (annotation != null) {
            return annotation;
        }

        // 元注解访问链
        Deque<Annotation> visited = new LinkedList<>();
        // 遍历查找该元素的其他注解上的元注解
        for (Annotation other : annotatedElement.getAnnotations()) {
            // 如果从该元注解没有找到指定注解，则清空该访问链，继续该元素其他注解上查找
            if ((annotation = findMetaAnnotation(other, targetAnnotationType, visited)) == null) {
                visited.clear();
                continue;
            }
            break;
        }

        // 如果注解不存在，抛出异常
        if (annotation == null) {
            String name = null;
            if (annotatedElement instanceof Class) {
                name = ((Class) annotatedElement).getName();
            } else if (annotatedElement instanceof Constructor) {
                name = ((Constructor) annotatedElement).getName();
            } else if (annotatedElement instanceof Method) {
                name = ((Method) annotatedElement).getName();
            } else if (annotatedElement instanceof Field) {
                name = ((Field) annotatedElement).getName();
            }
            String msg = String.format("在'@%s'注解上的所有元注解中找不到'@%s'元注解",
                    name, targetAnnotationType.getName());
            throw new IllegalArgumentException(msg);
        }

        // 属性值提取器
        AttributeValueExtractor valueExtractor = DefaultAttributeExtractor.from(annotatedElement, annotation, visited);
        // 包装为动态代理对象，使其实现组合注解功能
        return (A) Proxy.newProxyInstance(annotation.getClass().getClassLoader(),
                new Class<?>[]{annotation.annotationType()},
                new SynthesizedAnnotationInvocationHandler(valueExtractor));
    }

    /**
     * 根据元注解类型递归查找指定注解的元注解
     *
     * @param annotation 注解
     * @param targetType 元注解类型
     * @param visited    访问链
     * @return 元注解
     */
    private static <A extends Annotation> A findMetaAnnotation(Annotation annotation, Class<A> targetType, Deque<Annotation> visited) {
        // 添加注解至访问链
        visited.addLast(annotation);
        Class<? extends Annotation> annotationType = annotation.annotationType();
        A target = annotationType.getAnnotation(targetType);
        if (target == null) {
            for (Annotation other : annotationType.getAnnotations()) {
                // 如果是java自带的元注解，直接跳过
                if (annotationType.getName().startsWith("java.lang.annotation")) {
                    continue;
                }
                // 如果注解存在指定元注解，则跳出循环，否则，递归查找
                if ((target = annotationType.getAnnotation(targetType)) != null) {
                    visited.addLast(other);
                    break;
                }
                // 递归查找
                if ((target = findMetaAnnotation(other, targetType, visited)) != null) {
                    break;
                }
                // 没有找到则移除访问链中最后一个无用的访问
                visited.removeLast();
            }
        }
        return target;
    }

    /**
     * 获取注解中的所有属性方法
     *
     * @param annotationType 注解类型
     * @return 属性方法列表
     */
    public static List<Method> getAttributeMethods(Class<? extends Annotation> annotationType) {
        List<Method> methods = new ArrayList<>();
        for (Method method : annotationType.getDeclaredMethods()) {
            //判断方法是否为注解的属性方法
            if (method != null && method.getParameterCount() == 0 && method.getReturnType() != void.class) {
                methods.add(method);
            }
        }
        return methods;
    }
}
