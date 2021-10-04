package io.github.dovemy.hami.mybatisplus;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * MybatisPlus配置属性
 *
 * @author xuhaoming
 * @date 2021/10/1
 */
@Data
@ConfigurationProperties("hami.mybatis-plus")
public class HamiMybatisPlusProperties {

    /**
     * 数据库类型，默认用MySQL
     * {@link com.baomidou.mybatisplus.annotation.DbType}
     */
    private String dbType = "mysql";

}
