package io.github.dovemy.hami.redis;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

/**
 * redis配置属性
 *
 * @author xuhaoming
 * @date 2021/10/3
 */
@Data
@ConfigurationProperties("hami.redis")
public class HamiRedisProperties {

    /**
     * key统一前缀，例如"hami"
     */
    private String keyPrefix;

    /**
     * 缓存默认存活时间，单位秒，默认永不过期
     */
    private Integer cacheDefaultTtl = -1;

    /**
     * 缓存存活时间对[cacheName, expireSeconds] 例如"MINUTE_20":1200
     */
    private Map<String, Integer> cacheTtlEntry;

}
