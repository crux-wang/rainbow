package ren.crux.rainbow.core.utils;

import lombok.extern.slf4j.Slf4j;
import ren.crux.rainbow.core.model.Annotation;
import ren.crux.rainbow.core.model.TypeDesc;

import java.lang.reflect.*;
import java.util.Arrays;
import java.util.Map;

/**
 * @author wangzhihui
 */
@Slf4j
public class EntryUtils {

    @SuppressWarnings("unchecked")
    public static Annotation process(java.lang.annotation.Annotation annotation) {
        Class<? extends java.lang.annotation.Annotation> annotationType = annotation.annotationType();
        Annotation annotationDetail = new Annotation();
        annotationDetail.setName(annotationType.getSimpleName());
        annotationDetail.setType(annotationType.getCanonicalName());
        InvocationHandler invocationHandler = Proxy.getInvocationHandler(annotation);
        try {
            Field field = invocationHandler.getClass().getDeclaredField("memberValues");
            field.setAccessible(true);
            Map<String, Object> memberValues = (Map<String, Object>) field.get(invocationHandler);
            annotationDetail.setAttribute(memberValues);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return annotationDetail;
    }

    public static TypeDesc build(Field field) {
        return new TypeDesc(field.getGenericType().getTypeName(), getActualTypeArguments(field.getGenericType()));
    }

    public static TypeDesc build(Parameter parameter) {
        return new TypeDesc(parameter.getParameterizedType().getTypeName(), getActualTypeArguments(parameter.getParameterizedType()));
    }

    public static TypeDesc build(Type type) {
        return new TypeDesc(type.getTypeName(), getActualTypeArguments(type));
    }

    public static String[] getActualTypeArguments(Type type) {
        if (type instanceof ParameterizedType) {
            Type[] actualTypeArguments = ((ParameterizedType) (type)).getActualTypeArguments();
            if (actualTypeArguments != null) {
                return Arrays.stream(actualTypeArguments).map(Type::getTypeName).toArray(String[]::new);
            }
        }
        return null;
    }


}
