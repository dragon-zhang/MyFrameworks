package com.example.creativework.IdempotentCheckAnnotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

/**
 * @author SuccessZhang
 * @version 1.0
 * @date 2020/2/6 2:37 PM
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface IdeCheck {

    /**
     * {@link Adjudicator}的实现类
     */
    Class<? extends Adjudicator> adjudicator();

    /**
     * Redis缓存过期时间, 默认10分钟, 单位: 秒
     */
    int seconds() default 600;

    /**
     * 配置的时间单位, 默认为秒
     *
     * @return 时间单位
     */
    TimeUnit timeUnit() default TimeUnit.SECONDS;

}
