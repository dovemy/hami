package io.github.dovemy.hami.redis;

import java.util.HashMap;

/**
 * Redis缓存空间过期时间配置
 *
 * @author xuhaoming
 * @date 2021/10/03 22:00
 */
public class RedisCacheExpireConfig {

    public static final String SECOND_1 = "SECOND_1";
    public static final String SECOND_5 = "SECOND_5";
    public static final String SECOND_10 = "SECOND_10";
    public static final String SECOND_15 = "SECOND_15";
    public static final String SECOND_30 = "SECOND_30";

    public static final String MINUTE_1 = "MINUTE_1";
    public static final String MINUTE_3 = "MINUTE_3";
    public static final String MINUTE_5 = "MINUTE_5";
    public static final String MINUTE_15 = "MINUTE_15";
    public static final String MINUTE_30 = "MINUTE_30";

    public static final String HOUR_1 = "HOUR_1";
    public static final String HOUR_2 = "HOUR_2";
    public static final String HOUR_6 = "HOUR_6";
    public static final String HOUR_12 = "HOUR_12";

    public static final String DAY_1 = "DAY_1";
    public static final String DAY_3 = "DAY_3";
    public static final String DAY_10 = "DAY_10";
    public static final String DAY_30 = "DAY_30";

    /**
     * 过期时间键值对 k-v : 名称-过期时间(秒钟)
     */
    public static final HashMap<String, Integer> EXPIRE_TIME_MAP = new HashMap<String, Integer>(){
        {
            put(SECOND_1, 1);
            put(SECOND_5, 5);
            put(SECOND_10, 10);
            put(SECOND_15, 15);
            put(SECOND_30, 30);

            put(MINUTE_1, 60);
            put(MINUTE_3, 3 * 60);
            put(MINUTE_5, 5 * 60);
            put(MINUTE_15, 15 * 60);
            put(MINUTE_30, 30 * 60);

            put(HOUR_1, 60 * 60);
            put(HOUR_2, 2 * 60 * 60);
            put(HOUR_6, 6 * 60 * 60);
            put(HOUR_12, 12 * 60 * 60);

            put(DAY_1, 60 * 24 * 60);
            put(DAY_3, 60 * 24 * 3 * 60);
            put(DAY_10, 60 * 24 * 10 * 60);
            put(DAY_30, 60 * 24 * 30 * 60);

        }
    };

}
