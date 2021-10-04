package io.github.dovemy.hami.mybatisplus;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * MybatisPlus自动配置类
 *
 * @author xuhaoming
 * @date 2021/10/1
 */
@Configuration
@EnableConfigurationProperties(HamiMybatisPlusProperties.class)
public class HamiMybatisPlusAutoConfiguration {

    private final HamiMybatisPlusProperties mybatisPlusProperties;

    @Autowired
    public HamiMybatisPlusAutoConfiguration(HamiMybatisPlusProperties mybatisPlusProperties) {
        this.mybatisPlusProperties = mybatisPlusProperties;
    }

    /**
     * 注册拦截器
     */
    @Bean
    @ConditionalOnMissingBean(MybatisPlusInterceptor.class)
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        DbType dbType = DbType.getDbType(mybatisPlusProperties.getDbType());
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(dbType));
        return interceptor;
    }

}
