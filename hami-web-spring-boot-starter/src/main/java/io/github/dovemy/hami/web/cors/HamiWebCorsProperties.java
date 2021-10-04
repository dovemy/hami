package io.github.dovemy.hami.web.cors;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 跨域配置属性
 *
 * @author xuhaoming
 * @date 2021/9/30 17:35
 */
@Data
@ConfigurationProperties("hami.web.cors")
public class HamiWebCorsProperties {

    private boolean enable = false;

    private String allowedHeader = "*";

    private String allowedOrigin = "*";

}
