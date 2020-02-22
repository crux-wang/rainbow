package ren.crux.rainbow.core.utils;

import com.sun.javadoc.FieldDoc;
import com.sun.javadoc.MethodDoc;
import com.sun.javadoc.ParamTag;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import ren.crux.rainbow.javadoc.utils.JavaDocHelper;
import ren.crux.raonbow.common.model.RequestParam;
import ren.crux.raonbow.common.model.TypeDesc;

import java.lang.reflect.*;
import java.util.*;

/**
 * @author wangzhihui
 */
@Slf4j
public class EntryUtils {

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
        Optional<Type[]> optional = JavaDocHelper.getActualTypeArguments(type);
        if (optional.isPresent()) {
            Type[] actualTypeArguments = optional.get();
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

    public static TypeDesc build(FieldDoc fieldDoc) {
        return build(fieldDoc.type());
    }

    public static TypeDesc build(MethodDoc methodDoc) {
        return build(methodDoc.returnType());
    }

    public static TypeDesc build(com.sun.javadoc.Type type) {
        return new TypeDesc(type.typeName(), getActualTypeDesc(type), type.simpleTypeName(), type.qualifiedTypeName());
    }

    public static TypeDesc[] getActualTypeDesc(com.sun.javadoc.Type type) {
        Optional<com.sun.javadoc.Type[]> optional = JavaDocHelper.getActualTypeArguments(type);
        if (optional.isPresent()) {
            com.sun.javadoc.Type[] actualTypeArguments = optional.get();
            return Arrays.stream(actualTypeArguments).map(typ -> {
                TypeDesc typeDesc = new TypeDesc();
                typeDesc.setName(typ.typeName());
                typeDesc.setActualParamTypes(getActualTypeDesc(typ));
                if (typeDesc.getActualParamTypes() != null) {
                    Type rawType = ((ParameterizedType) typ).getRawType();
                    typeDesc.setType(rawType.getTypeName());
                    typeDesc.setSimpleName(StringUtils.substringAfterLast(rawType.getTypeName(), "."));
                } else {
                    typeDesc.setType(typ.typeName());
                    if (typ.typeName().contains(".")) {
                        typeDesc.setSimpleName(StringUtils.substringAfterLast(typ.typeName(), "."));
                    } else {
                        typeDesc.setSimpleName(typ.typeName());
                    }
                }
                return typeDesc;
            }).toArray(TypeDesc[]::new);
        }
        return null;
    }


    public static Map<String, ParamTag> buildParamMap(MethodDoc methodDoc, List<RequestParam> params) {
        Map<String, ParamTag> map = new HashMap<>(params.size());
        ParamTag[] paramTags = methodDoc.paramTags();
        for (int i = 0; i < params.size(); i++) {
            RequestParam param = params.get(i);
            ParamTag paramTag = paramTags[i];
            param.setName(paramTag.parameterName());
            map.put(param.getName(), paramTag);
        }
        return map;
    }
}
