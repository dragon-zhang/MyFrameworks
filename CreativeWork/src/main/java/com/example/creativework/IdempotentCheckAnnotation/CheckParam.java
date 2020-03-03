package com.example.creativework.IdempotentCheckAnnotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>文件名称：com.vdian.vclub.vtoolkit.commons.annotation.CheckParam</p>
 * <p>文件描述：标明需要进行幂等性校验参数的注解</p>
 * <p>版权所有： (C)2011-2099 微店</p>
 * <p>内容摘要： </p>
 * <p>其他说明： </p>
 * <p>完成日期：2020/2/6 2:37 PM</p>
 *
 * @author SuccessZhang
 * @version 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface CheckParam {
}
