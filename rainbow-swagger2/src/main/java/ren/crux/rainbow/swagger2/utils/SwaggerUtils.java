package ren.crux.rainbow.swagger2.utils;

import org.springframework.util.ReflectionUtils;
import springfox.documentation.RequestHandler;
import springfox.documentation.spi.service.contexts.OperationContext;
import springfox.documentation.spi.service.contexts.RequestMappingContext;

import java.lang.reflect.Field;

/**
 * SwaggerUtils
 *
 * @author wangzhihui
 **/
public class SwaggerUtils {

    public static RequestMappingContext getRequestMappingContext(OperationContext context) {
        try {
            Field field = OperationContext.class.getDeclaredField("requestContext");
            ReflectionUtils.makeAccessible(field);
            return (RequestMappingContext) ReflectionUtils.getField(field, context);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    public static RequestHandler getRequestHandler(OperationContext context) {
        return getRequestHandler(getRequestMappingContext(context));
    }

    public static RequestHandler getRequestHandler(RequestMappingContext context) {
        try {
            Field field = RequestMappingContext.class.getDeclaredField("handler");
            ReflectionUtils.makeAccessible(field);
            return (RequestHandler) ReflectionUtils.getField(field, context);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

}
