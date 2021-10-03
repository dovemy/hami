package io.github.dovemy.hami.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * Redis锁服务
 *
 * @author xuhaoming
 * @date 2021/10/3
 */
@Component
@ConditionalOnBean(RedisTemplate.class)
@SuppressWarnings("all")
public class RedisLockService {

    private final RedisTemplate redisTemplate;

    @Autowired
    public RedisLockService(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * 尝试获取锁
     *
     * @param key     业务标识key
     * @param timeout 锁存活时间，用与超时自动释放锁
     * @param unit    时间单位
     * @return 是否获取锁
     */
    public boolean lock(String key, long timeout, TimeUnit unit) {
        String redisKey = "redis-lock:" + key;
        // 原子操作 setnx
        return redisTemplate.opsForValue().setIfAbsent(redisKey, 1, timeout, unit);
    }


    /**
     * 释放锁
     *
     * @param key 业务标识key
     * @return 是否成功删除值
     */
    public boolean unlock(String key) {
        String redisKey = "lock:" + key;
        return redisTemplate.delete(redisKey);
    }

}
