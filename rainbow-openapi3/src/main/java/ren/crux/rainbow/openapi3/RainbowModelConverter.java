//package ren.crux.rainbow.openapi3;
//
//import com.fasterxml.jackson.databind.JavaType;
//import io.swagger.v3.core.converter.AnnotatedType;
//import io.swagger.v3.core.converter.ModelConverter;
//import io.swagger.v3.core.converter.ModelConverterContext;
//import io.swagger.v3.core.util.Json;
//import io.swagger.v3.oas.models.media.ObjectSchema;
//import io.swagger.v3.oas.models.media.Schema;
//import org.springframework.core.Ordered;
//import org.springframework.core.annotation.Order;
//import org.springframework.stereotype.Component;
//import ren.crux.rainbow.common.utils.RuntimeJavadocUtils;
//
//import java.lang.reflect.Field;
//import java.util.Iterator;
//import java.util.Map;
//
///**
// * RainbowModelConverter
// *
// * @author wangzhihui
// **/
//@Order(Ordered.HIGHEST_PRECEDENCE)
//@Component
//public class RainbowModelConverter implements ModelConverter {
//    /**
//     * @param type
//     * @param context
//     * @param chain   the chain of model converters to try if this implementation cannot process
//     * @return null if this ModelConverter cannot convert the given Type
//     */
//    @Override
//    public Schema resolve(AnnotatedType type, ModelConverterContext context, Iterator<ModelConverter> chain) {
//        if (chain.hasNext()) {
//            Schema<?> resolvedSchema = chain.next().resolve(type, context, chain);
//            if (resolvedSchema instanceof ObjectSchema) {
//                JavaType javaType = Json.mapper().constructType(type.getType());
//                Class<?> rawClass = javaType.getRawClass();
//                Map<String, Schema> properties = resolvedSchema.getProperties();
//                if (properties != null) {
//                    properties.forEach((name, schema) -> {
//                        try {
//                            Field field = rawClass.getField(name);
//                            String comment = RuntimeJavadocUtils.getComment(field);
//                            schema.description(comment);
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    });
//                }
//            }
//            return resolvedSchema;
//        } else {
//            return null;
//        }
//    }
//}
