package io.github.dovemy.hami.web.swagger;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

import java.util.Map;

/**
 * Swagger自动配置类
 *
 * @author xuhaoming
 * @date 2021/10/8 11:57
 */
@Configuration
@EnableSwagger2WebMvc
@EnableConfigurationProperties(HamiWebSwaggerProperties.class)
public class HamiSwaggerAutoConfiguration implements BeanFactoryAware {

    private BeanFactory beanFactory;

    private final HamiWebSwaggerProperties hamiWebSwaggerProperties;

    public HamiSwaggerAutoConfiguration(HamiWebSwaggerProperties hamiWebSwaggerProperties) {
        this.hamiWebSwaggerProperties = hamiWebSwaggerProperties;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    /**
     * 执行配置：接口分组、是否禁用
     */
    @Bean
    public void executeSwaggerConfig() {

        ConfigurableBeanFactory configurableBeanFactory = (ConfigurableBeanFactory) this.beanFactory;

        Map<String, HamiWebSwaggerProperties.DocketInfo> dockets = hamiWebSwaggerProperties.getDockets();
        if (dockets != null && !dockets.isEmpty()) {
            for (String docketName : dockets.keySet()) {
                HamiWebSwaggerProperties.DocketInfo docketInfo = dockets.get(docketName);

                ApiInfo apiInfo = new ApiInfoBuilder()
                        .title(docketInfo.getDocTitle())
                        .contact(docketInfo.getAuthor())
                        .description(docketInfo.getDescription())
                        .version(docketInfo.getVersion())
                        .build();

                Docket docket = new Docket(DocumentationType.SWAGGER_2)
                        .apiInfo(apiInfo)
                        .groupName(docketInfo.getGroupName())
                        .select()
                        .apis(RequestHandlerSelectors.basePackage(docketInfo.getBasePackage()))
                        .paths(PathSelectors.any())
                        .build();

                configurableBeanFactory.registerSingleton(docketName, docket);

            }
        }

    }

}
