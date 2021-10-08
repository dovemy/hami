package io.github.dovemy.hami.web.swagger;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Swagger配置属性
 *
 * @author xuhaoming
 * @date 2021/10/8 14:47
 */
@Data
@ConfigurationProperties("hami.web.swagger")
public class HamiWebSwaggerProperties {

    /**
     * 分组信息
     */
    private Map<String, DocketInfo> dockets = new LinkedHashMap<>();


    /**
     * 分组信息
     */
    @Data
    public static class DocketInfo {

        /**
         * 分组名称
         */
        private String groupName = "";

        /**
         * 包路径
         */
        private String basePackage = "";

        /**
         * 文档标题
         */
        private String docTitle = "Api Documentation";

        /**
         * 作者名
         */
        private String author = "";

        /**
         * 描述
         */
        private String description = "";

        /**
         * 接口版本
         */
        private String version = "";

    }

}
