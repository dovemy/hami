package io.github.dovemy.hami.redis;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Redis自动配置类
 *
 * @author xuhaoming
 * @date 2021/10/3
 */
@Configuration
@EnableCaching
@EnableConfigurationProperties(HamiRedisProperties.class)
public class HamiRedisAutoConfiguration {

    /**
     * 默认日期时间格式
     */
    private static final String DEFAULT_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * 默认日期格式
     */
    private static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";

    /**
     * 默认时间格式
     */
    private static final String DEFAULT_TIME_FORMAT = "HH:mm:ss";


    private final HamiRedisProperties redisProperties;

    @Autowired
    public HamiRedisAutoConfiguration(HamiRedisProperties redisProperties) {
        this.redisProperties = redisProperties;
    }


    @Bean
    @ConditionalOnMissingBean(name = {"redisTemplate"})
    @SuppressWarnings("all")
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> template = new RedisTemplate<String, Object>();
        template.setConnectionFactory(factory);
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer() {
            @Override
            public byte[] serialize(String string) {
                String keyPrefix = redisProperties.getKeyPrefix();
                if (keyPrefix != null) {
                    if (!keyPrefix.endsWith(":")) {
                        keyPrefix += ":";
                    }
                    return super.serialize(keyPrefix + string);
                } else {
                    return super.serialize(string);
                }
            }
        };
        // key都采用String的序列化方式
        template.setKeySerializer(stringRedisSerializer);
        template.setHashKeySerializer(stringRedisSerializer);
        // value的序列化方式采用jackson
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        // 解决查询缓存转换异常的问题
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        objectMapper.activateDefaultTyping(objectMapper.getPolymorphicTypeValidator(), ObjectMapper.DefaultTyping.NON_FINAL);
        // 忽略多余/不存在的属性
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        // 添加java8时间相关序列化/反序列化处理
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_FORMAT)));
        javaTimeModule.addSerializer(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT)));
        javaTimeModule.addSerializer(LocalTime.class, new LocalTimeSerializer(DateTimeFormatter.ofPattern(DEFAULT_TIME_FORMAT)));
        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_FORMAT)));
        javaTimeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer(DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT)));
        javaTimeModule.addDeserializer(LocalTime.class, new LocalTimeDeserializer(DateTimeFormatter.ofPattern(DEFAULT_TIME_FORMAT)));
        objectMapper.registerModule(javaTimeModule);

        jackson2JsonRedisSerializer.setObjectMapper(objectMapper);
        jackson2JsonRedisSerializer.setObjectMapper(objectMapper);
        template.setValueSerializer(jackson2JsonRedisSerializer);
        template.setHashValueSerializer(jackson2JsonRedisSerializer);
        template.afterPropertiesSet();
        return template;
    }


    /**
     * 缓存管理器
     */
    @Bean
    @ConditionalOnBean(value = {RedisTemplate.class})
    @ConditionalOnMissingBean(CacheManager.class)
    public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory, RedisTemplate redisTemplate) {

        // 配置默认过期时间和序列化机制
        RedisCacheConfiguration defaultCacheConfig = RedisCacheConfiguration.defaultCacheConfig()
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(redisTemplate.getKeySerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(redisTemplate.getValueSerializer()))
                // 不使用自带的cacheName::前缀
                .disableKeyPrefix();
        // 默认缓存过期时间
        Integer cacheDefaultTtl = redisProperties.getCacheDefaultTtl();
        if (cacheDefaultTtl > 0) {
            defaultCacheConfig.entryTtl(Duration.ofSeconds(cacheDefaultTtl));
        }

        // 内置缓存空间过期配置
        Set<String> cacheNames =  new HashSet<>();
        Map<String, RedisCacheConfiguration> configMap = new HashMap<>(RedisCacheExpireConfig.EXPIRE_TIME_MAP.size());
        for (Map.Entry<String, Integer> entry : RedisCacheExpireConfig.EXPIRE_TIME_MAP.entrySet()) {
            String cacheName = entry.getKey();
            Integer expireMinute = entry.getValue();
            cacheNames.add(cacheName);
            configMap.put(cacheName, defaultCacheConfig.entryTtl(Duration.ofSeconds(expireMinute)));
        }
        // 用户自定义缓存时间配置
        Map<String, Integer> cacheTtlEntry = redisProperties.getCacheTtlEntry();
        if (cacheTtlEntry != null && cacheTtlEntry.size() > 0) {
            for (String key : cacheTtlEntry.keySet()) {
                cacheNames.add(key);
                configMap.put(key, defaultCacheConfig.entryTtl(Duration.ofSeconds(cacheTtlEntry.get(key))));
            }
        }

        return RedisCacheManager.builder(redisConnectionFactory)
                .cacheDefaults(defaultCacheConfig)
                .initialCacheNames(cacheNames)
                .withInitialCacheConfigurations(configMap)
                .build();

    }


}
