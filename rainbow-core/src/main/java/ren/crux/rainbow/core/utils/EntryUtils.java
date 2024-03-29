package ren.crux.rainbow.core.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import ren.crux.rainbow.core.model.Annotation;
import ren.crux.rainbow.core.model.TypeDesc;

import java.lang.reflect.*;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author wangzhihui
 */
@Slf4j
public class EntryUtils {

    public static List<Field> getAllFields(Class<?> cls) {
        List<Field> fields = new LinkedList<>();
        while (cls != null && !Object.class.equals(cls)) {
            Field[] declaredFields = cls.getDeclaredFields();
            fields.addAll(Arrays.asList(declaredFields));
            cls = cls.getSuperclass();
        }
        return fields;
    }

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
        Type genericType = field.getGenericType();
        return new TypeDesc(genericType.getTypeName(), getActualTypeDesc(genericType), field.getType().getSimpleName());
    }

    public static TypeDesc build(Parameter parameter) {
        Type parameterizedType = parameter.getParameterizedType();
        return new TypeDesc(parameterizedType.getTypeName(), getActualTypeDesc(parameterizedType), parameter.getType().getSimpleName());
    }

    public static TypeDesc build(Method method) {
        Class<?> returnType = method.getReturnType();
        Type genericReturnType = method.getGenericReturnType();
        return new TypeDesc(returnType.getTypeName(), getActualTypeDesc(genericReturnType), returnType.getSimpleName(), genericReturnType.getTypeName());
    }

    public static TypeDesc[] getActualTypeDesc(Type type) {
        Type[] actualTypeArguments = getActualTypeArguments(type);
        if (actualTypeArguments != null) {
            return Arrays.stream(actualTypeArguments).map(typ -> {
                TypeDesc typeDesc = new TypeDesc();
                typeDesc.setName(typ.getTypeName());
                typeDesc.setActualParamTypes(getActualTypeDesc(typ));
                if (typeDesc.getActualParamTypes() != null) {
                    Type rawType = ((ParameterizedType) typ).getRawType();
                    typeDesc.setType(rawType.getTypeName());
                    typeDesc.setSimpleName(StringUtils.substringAfterLast(rawType.getTypeName(), "."));
                }
                return typeDesc;
            }).toArray(TypeDesc[]::new);
        }
        return null;
    }

    public static Type[] getActualTypeArguments(Type type) {
        if (type instanceof ParameterizedType) {
            Type[] actualTypeArguments = ((ParameterizedType) (type)).getActualTypeArguments();
            if (actualTypeArguments != null) {
                return Arrays.stream(actualTypeArguments).toArray(Type[]::new);
            }
        }
        return null;
    }

}
