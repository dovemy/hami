package com.xuhaoming.hami.web.cors;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * @author xuhaoming
 * @date 2021/9/30 17:39
 */
@Configuration
@EnableConfigurationProperties(HamiWebCorsProperties.class)
public class HamiWebCorsAutoConfiguration {

    private final HamiWebCorsProperties corsProperties;

    public HamiWebCorsAutoConfiguration(HamiWebCorsProperties corsProperties) {
        this.corsProperties = corsProperties;
    }

    @Bean
    @ConditionalOnMissingBean(CorsFilter.class)
    @ConditionalOnProperty(prefix = "hami.web.cors", name = "enable", havingValue = "true", matchIfMissing = false)
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        if ("*".equals(corsProperties.getAllowedOrigin())) {
            config.addAllowedOrigin(corsProperties.getAllowedOrigin());
        } else {
            config.addAllowedOriginPattern("*");
        }
        config.addAllowedHeader("*");
        config.addExposedHeader("*");
        config.addAllowedMethod("*");
        config.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource corsConfigurationSource = new UrlBasedCorsConfigurationSource();
        corsConfigurationSource.registerCorsConfiguration("/**", config);
        return new CorsFilter(corsConfigurationSource);
    }

}
