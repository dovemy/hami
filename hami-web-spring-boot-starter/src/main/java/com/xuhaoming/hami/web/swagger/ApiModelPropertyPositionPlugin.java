package com.xuhaoming.hami.web.swagger;

import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.schema.ModelPropertyBuilderPlugin;
import springfox.documentation.spi.schema.contexts.ModelPropertyContext;
import springfox.documentation.swagger.common.SwaggerPluginSupport;
import springfox.documentation.swagger.schema.ApiModelProperties;

import java.lang.reflect.Field;
import java.util.Optional;

import static springfox.documentation.schema.Annotations.findPropertyAnnotation;


/**
 * <p>
 * Swagger模型字段排序插件
 * </p>
 * 泛型请手动写getter方法
 *
 * @author xuhaoming
 * @since 2020/12/21
 */
@Component
public class ApiModelPropertyPositionPlugin implements ModelPropertyBuilderPlugin {

    private Log log = LogFactory.getLog(getClass());

    @Override
    public boolean supports(DocumentationType delimiter) {
        return SwaggerPluginSupport.pluginDoesApply(delimiter);
    }

    @Override
    public void apply(ModelPropertyContext context) {

        Optional<BeanPropertyDefinition> beanPropertyDefinitionOpt = context.getBeanPropertyDefinition();
        Optional<ApiModelProperty> annotation = Optional.empty();

        if (context.getAnnotatedElement().isPresent()) {
            annotation = Optional.of(annotation.orElseGet(ApiModelProperties.findApiModePropertyAnnotation(context.getAnnotatedElement().get())::get));
        }

        if (context.getBeanPropertyDefinition().isPresent()) {
            annotation = Optional.of(annotation.orElseGet(findPropertyAnnotation(context.getBeanPropertyDefinition().get(), ApiModelProperty.class)::get));
        }

        if (beanPropertyDefinitionOpt.isPresent()) {
            BeanPropertyDefinition beanPropertyDefinition = beanPropertyDefinitionOpt.get();
            if (annotation.isPresent() && annotation.get().position() != 0) {
                return;
            }

            AnnotatedField field = beanPropertyDefinition.getField();
            Class<?> clazz = field.getDeclaringClass();
            Field[] declaredFields = clazz.getDeclaredFields();
            Field declaredField;

            try {
                declaredField = clazz.getDeclaredField(field.getName());
            } catch (NoSuchFieldException | SecurityException e) {
                log.error("", e);
                return;
            }

            int indexOf = -1;
            for (int i = 0; i < declaredFields.length; i++) {
                if (declaredFields[i].equals(declaredField)) {
                    indexOf = i;
                    break;
                }
            }
            if (indexOf != -1) {
                context.getBuilder().position(indexOf);
            }

        }

    }

}
