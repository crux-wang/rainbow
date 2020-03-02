package ren.crux.rainbow.swagger2.reader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import ren.crux.rainbow.common.utils.RuntimeJavadocUtils;
import ren.crux.rainbow.swagger2.RainbowSwaggerPluginSupport;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.schema.ModelPropertyBuilderPlugin;
import springfox.documentation.spi.schema.contexts.ModelPropertyContext;
import springfox.documentation.spring.web.DescriptionResolver;
import springfox.documentation.swagger.common.SwaggerPluginSupport;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * RainbowOperationModelsProvider
 *
 * @author wangzhihui
 **/
@Component
@Order(RainbowSwaggerPluginSupport.RAINBOW_SWAGGER_PLUGIN_ORDER)
public class RainbowApiModelPropertyPropertyBuilder implements ModelPropertyBuilderPlugin {

    private final DescriptionResolver descriptions;

    @Autowired
    public RainbowApiModelPropertyPropertyBuilder(DescriptionResolver descriptions) {
        this.descriptions = descriptions;
    }

    @Override
    public void apply(ModelPropertyContext context) {
        context.getBeanPropertyDefinition().toJavaUtil().ifPresent(def -> {
            if (def.hasField()) {
                Field field = def.getField().getAnnotated();
                String cmt = RuntimeJavadocUtils.getComment(field);
                context.getBuilder().description(descriptions.resolve(cmt));
            } else if (def.hasGetter()) {
                Method method = def.getGetter().getAnnotated();
                String cmt = RuntimeJavadocUtils.getComment(method);
                context.getBuilder().description(descriptions.resolve(cmt));
            }
        });
    }

    @Override
    public boolean supports(DocumentationType delimiter) {
        return SwaggerPluginSupport.pluginDoesApply(delimiter);
    }

}
