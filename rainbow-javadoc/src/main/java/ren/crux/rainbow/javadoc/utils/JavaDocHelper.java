package ren.crux.rainbow.javadoc.utils;

import com.sun.javadoc.*;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import ren.crux.rainbow.javadoc.reader.JavaDocReader;

import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Java 文档工具类
 *
 * @author wangzhihui
 */
public class JavaDocHelper {

    public static final String OBJECT_TYPE_NAME = Object.class.getTypeName();

    /**
     * 获取所有属性映射 （ 包括父类 ）
     *
     * @param classDoc 类文档
     * @return name -> fieldDoc
     */
    public static Map<String, FieldDoc> getAllFieldDoc(ClassDoc classDoc) {
        if (classDoc == null) {
            return Collections.emptyMap();
        }
        Map<String, FieldDoc> map = Arrays.stream(classDoc.fields()).collect(Collectors.toMap(Doc::name, fd -> fd));
        ClassDoc superclass = classDoc.superclass();
        if (superclass == null) {
            return map;
        }
        // 递归并用子类属性替换父类同名的属性
        Map<String, FieldDoc> superMap = getAllFieldDoc(superclass);
        if (superMap.isEmpty()) {
            return map;
        }
        superMap.putAll(map);
        return superMap;
    }

    /**
     * 获取所有属性 （包括父类）
     *
     * @param cls 类
     * @return name -> field
     */
    public static Map<String, Field> getAllField(Class<?> cls) {
        if (cls == null) {
            return Collections.emptyMap();
        }
        Map<String, Field> map = Arrays.stream(cls.getDeclaredFields()).collect(Collectors.toMap(Field::getName, fd -> fd));
        Class<?> superclass = cls.getSuperclass();
        if (superclass == null) {
            return map;
        }
        // 递归并用子类属性替换父类同名的属性
        Map<String, Field> superMap = getAllField(superclass);
        if (superMap.isEmpty()) {
            return map;
        }
        superMap.putAll(map);
        return superMap;
    }

    /**
     * 获取注解 {@code value} 值
     *
     * @param value 注解值
     * @return {@code value} 值
     */
    public static Object getValue(AnnotationValue value) {
        if (value.value() instanceof AnnotationValue[]) {
            return Arrays.stream(((AnnotationValue[]) value.value())).map(AnnotationValue::value).toArray();
        } else if (value.value() instanceof AnnotationValue) {
            return ((AnnotationValue) value.value()).value();
        } else {
            return value.value();
        }
    }

    /**
     * 构建方法签名
     * <p>
     * 与{@link #sign(Method)} 签名结果保持一致
     *
     * @param methodDoc 方法文档
     * @return 方法签名
     */
    public static String sign(MethodDoc methodDoc) {
        return StringUtils.replace(methodDoc.toString(), " ", "");
    }

    /**
     * 构建方法签名
     * <p>
     * 与{@link #sign(MethodDoc)} 签名结果保持一致
     *
     * @param method 方法
     * @return 方法签名
     */
    public static String sign(Method method) {
        String methodStr = method.toString();
        return StringUtils.substringBetween(StringUtils.substringAfter(methodStr, " "), " ", "(") + "(" + StringUtils.substringBetween(methodStr, "(", ")") + ")";
    }

    /**
     * 获取所有公开方法文档 （不包括父类）
     *
     * @param classDoc 类文档
     * @return method sign -> methodDOoc
     */
    public static Map<String, MethodDoc> getAllPublicMethodDocs(ClassDoc classDoc) {
        return Arrays.stream(classDoc.methods(true))
                .collect(Collectors.toMap(JavaDocHelper::sign, md -> md));
    }

    /**
     * 获取无参且公开的方法列表（包含父类）
     *
     * @param classDoc 类文档
     * @return method sign -> methodDOoc
     */
    public static Map<String, MethodDoc> getNoArgPublicMethodDocs(ClassDoc classDoc) {
        if (classDoc == null) {
            return Collections.emptyMap();
        }
        Map<String, MethodDoc> map = Arrays.stream(classDoc.methods(true))
                .filter(JavaDocHelper::isNoArgMethod)
                .collect(Collectors.toMap(JavaDocHelper::sign, md -> md));
        ClassDoc superclass = classDoc.superclass();
        if (superclass == null) {
            return map;
        }
        // 递归并用子类方法替换父类同名的方法
        Map<String, MethodDoc> superMap = getNoArgPublicMethodDocs(superclass);
        if (superMap.isEmpty()) {
            return map;
        }
        superMap.putAll(map);
        return superMap;
    }

    /**
     * 是否是无参的方法
     *
     * @param methodDoc 方法文档
     * @return 是否是无参的方法
     */
    public static boolean isNoArgMethod(MethodDoc methodDoc) {
        return methodDoc.parameters().length == 0;
    }

    /**
     * 获取注解属性
     *
     * @param annotation 注解
     * @return name -> value
     */
    @SuppressWarnings("unchecked")
    public static Map<String, Object> getAttributes(Annotation annotation) {
        InvocationHandler invocationHandler = Proxy.getInvocationHandler(annotation);
        try {
            Field field = invocationHandler.getClass().getDeclaredField("memberValues");
            field.setAccessible(true);
            return (Map<String, Object>) field.get(invocationHandler);
        } catch (NoSuchFieldException | IllegalAccessException ignored) {
        }
        return Collections.emptyMap();
    }

    /**
     * 获取注解属性
     *
     * @param annotation 注解
     * @return name -> value
     */
    public static Map<String, Object> getAttributes(AnnotationDesc annotation) {
        Map<String, Object> attribute = null;
        for (AnnotationDesc.ElementValuePair elementValuePair : annotation.elementValues()) {
            AnnotationTypeElementDoc element = elementValuePair.element();
            Object value = JavaDocHelper.getValue(elementValuePair.value());
            if (attribute == null) {
                attribute = new HashMap<>(8);
            }
            attribute.put(element.name(), value == null ? JavaDocHelper.getValue(element.defaultValue()) : value);
        }
        return attribute == null ? Collections.emptyMap() : attribute;
    }

    /**
     * 获取泛型实际类型
     *
     * @param type 类型
     * @return 实际类型
     */
    public static Optional<Type[]> getActualTypeArguments(Type type) {
        if (type instanceof ParameterizedType) {
            Type[] actualTypeArguments = ((ParameterizedType) (type)).getActualTypeArguments();
            if (actualTypeArguments != null) {
                return Optional.of(Arrays.stream(actualTypeArguments).toArray(Type[]::new));
            }
        }
        return Optional.empty();
    }

    /**
     * 获取泛型实际类型
     *
     * @param type 类型
     * @return 实际类型
     */
    public static Optional<com.sun.javadoc.Type[]> getActualTypeArguments(com.sun.javadoc.Type type) {
        com.sun.javadoc.ParameterizedType parameterizedType = type.asParameterizedType();
        if (parameterizedType != null) {
            return Optional.ofNullable(parameterizedType.typeArguments());
        }
        return Optional.empty();
    }

    /**
     * 查找类文档
     *
     * @param className 类名
     * @return 类文档
     */
    public static Optional<ClassDoc> findClass(String className) {
        ClassDoc[] classes = JavaDocReader.Doclet.getRootDoc().classes();
        if (classes.length > 0) {
            return Optional.ofNullable(classes[0].findClass(className));
        }
        return Optional.empty();
    }

    /**
     * 查找类文档
     *
     * @param classNames 类名
     * @return 类文档
     */
    public static List<ClassDoc> findClass(Collection<String> classNames) {
        if (CollectionUtils.isEmpty(classNames)) {
            return Collections.emptyList();
        }
        return classNames.stream().map(JavaDocHelper::findClass).filter(Optional::isPresent).map(Optional::get).collect(Collectors.toList());
    }

}
