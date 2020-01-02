package ren.crux.rainbow.core.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import ren.crux.rainbow.core.model.Annotation;
import ren.crux.rainbow.core.model.TypeDesc;

import java.lang.reflect.*;
import java.util.*;

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
        return new TypeDesc(genericType.getTypeName(), getActualTypeDesc(genericType), field.getType().getSimpleName(), field.getType().getTypeName());
    }

    public static TypeDesc build(Parameter parameter) {
        Type parameterizedType = parameter.getParameterizedType();
        return new TypeDesc(parameterizedType.getTypeName(), getActualTypeDesc(parameterizedType), parameter.getType().getSimpleName(), parameter.getType().getTypeName());
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
                } else {
                    typeDesc.setType(typ.getTypeName());
                    if (typ.getTypeName().contains(".")) {
                        typeDesc.setSimpleName(StringUtils.substringAfterLast(typ.getTypeName(), "."));
                    } else {
                        typeDesc.setSimpleName(typ.getTypeName());
                    }
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

    public static void addEntryClassName(Set<String> entryClassNames, Collection<String> classNames) {
        if (classNames != null) {
            entryClassNames.addAll(classNames);
        }
    }

    public static void addEntryClassName(Set<String> entryClassNames, String className) {
        if (StringUtils.isNotBlank(className)) {
            className = StringUtils.substringBefore(className, "[");
            className = StringUtils.substringBefore(className, "<");
            if (StringUtils.equalsAny(className, "void", "int", "long", "float", "double", "byte", "boolean", "char", "short", "T", "E", "K", "V", "?")) {
                return;
            }
            if (StringUtils.startsWithAny(className, "java.lang.", "java.util.")) {
                return;
            }
            entryClassNames.add(className);
        }
    }

    public static void addEntryClassName(Set<String> entryClassNames, TypeDesc typeDesc) {
        if (typeDesc != null) {
            addEntryClassName(entryClassNames, typeDesc.getType());
            if (typeDesc.getActualParamTypes() != null) {
                for (TypeDesc actualParamType : typeDesc.getActualParamTypes()) {
                    addEntryClassName(entryClassNames, actualParamType);
                }
            }
        }
    }

    public static String sign(Method method) {
        String methodStr = method.toString();
        return StringUtils.substringBetween(StringUtils.substringAfter(methodStr, " "), " ", "(") + "(" + StringUtils.substringBetween(methodStr, "(", ")") + ")";
    }
}
