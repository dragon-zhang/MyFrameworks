package MySpringBoot.redis;

import javax.annotation.Nullable;
import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * @author SuccessZhang
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Nullable
@Documented
public @interface RedisCache {

    /**
     * 缓存的key
     */
    String key();

    /**
     * 缓存过期时间, 默认10分钟, 默认单位: s, 可以使用{@link #timeUnit()}配置所有的时间单位
     */
    int expire() default 10 * 60;

    /**
     * 配置的时间单位, 默认为秒
     *
     * @return 时间单位
     */
    TimeUnit timeUnit() default TimeUnit.SECONDS;
}
