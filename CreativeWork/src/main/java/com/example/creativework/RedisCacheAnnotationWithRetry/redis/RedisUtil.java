package com.example.creativework.RedisCacheAnnotationWithRetry.redis;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @author SuccessZhang
 */
@Slf4j
@Component
@RequiredArgsConstructor
@SuppressWarnings("unchecked")
public class RedisUtil {

    private final RedisTemplate redisTemplate;

    @Retryable
    public Object getCache(String key, String hashKey) {
        Object result = redisTemplate.opsForHash().get(key, hashKey);
        if (result != null) {
            return result;
        }
        log.error("Cache specified key{" + hashKey + "} hit missed");
        throw new RuntimeException("Cache specified key{" + hashKey + "} hit missed");
    }

    @Retryable
    public void putAndExpire(String key, String hashKey, Object mappedResult, int expire, TimeUnit timeUnit) {
        redisTemplate.opsForHash().put(key, hashKey, mappedResult);
        redisTemplate.expire(key, expire, timeUnit);
    }

    public boolean unlock(String mutexKey) {
        boolean unlocked = false;
        try {
            unlocked = (boolean) redisTemplate.execute((RedisCallback) connection -> {
                //只在键key不存在的情况下，将键key的值设置为value,若键key已经存在，则 SETNX 命令不做任何动作
                //命令在设置成功时返回1，设置失败时返回0
                return connection.setNX(mutexKey.getBytes(), "flag".getBytes());
            });
        } catch (Exception e) {
            log.error("redis request failed !", e);
        }
        return unlocked;
    }

    public Boolean expireInOneSecend(String mutexKey) {
        return redisTemplate.expire(mutexKey, 1000, TimeUnit.MILLISECONDS);
    }

    public Boolean delete(String mutexKey) {
        return redisTemplate.delete(mutexKey);
    }
}
