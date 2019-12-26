package ren.crux.rainbow.core.module;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.sun.javadoc.*;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import ren.crux.rainbow.core.ClassDocProvider;
import ren.crux.rainbow.core.model.TypeDesc;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Getter
public class Context {

    private final Map<String, Object> properties = new HashMap<>();
    private final Map<String, String> implMap = new HashMap<>();
    private final Set<String> entryClassNames = new HashSet<>();
    private final ClassDocProvider classDocProvider;
    private final Cache<String, Map<String, FieldDoc>> fieldDocCache = CacheBuilder.newBuilder().build();
    private final Map<String, MethodDoc> methodDocCache = new HashMap<>();
    private final Cache<String, Map<String, MethodDoc>> noArgsMethodDocCache = CacheBuilder.newBuilder().build();
    private final Map<String, Map<String, ParamTag>> paramTagCache = new HashMap<>();

    public Context(ClassDocProvider classDocProvider) {
        this.classDocProvider = classDocProvider;
    }

    public Optional<ClassDoc> getClassDoc(Class<?> cls) {
        return getClassDoc(cls.getTypeName());
    }

    public Optional<ClassDoc> getClassDoc(String type) {
        return classDocProvider.get(this, type);
    }

    public Optional<ParamTag> getParamTag(String methodSign, String paramName) {
        return Optional.ofNullable(getParamTags(methodSign).get(paramName));
    }

    public Map<String, ParamTag> getParamTags(String methodSign) {
        return paramTagCache.getOrDefault(methodSign, Collections.emptyMap());
    }

    public void addParamTags(String methodSign, Map<String, ParamTag> map) {
        paramTagCache.put(methodSign, map);
    }

    public Optional<FieldDoc> getClassFieldDoc(Class<?> cls, String name) {
        return Optional.ofNullable(getClassFieldDocs(cls).get(name));
    }

    public Map<String, FieldDoc> getClassFieldDocs(Class<?> cls) {
        return getClassDoc(cls).map(this::getClassFieldDocs).orElse(Collections.emptyMap());
    }

    public Map<String, FieldDoc> getClassFieldDocs(ClassDoc classDoc) {
        if (classDoc == null) {
            return Collections.emptyMap();
        }
        try {
            return fieldDocCache.get(classDoc.typeName(), () -> {
                Map<String, FieldDoc> map = Arrays.stream(classDoc.fields()).collect(Collectors.toMap(Doc::name, fd -> fd));
                ClassDoc superclass = classDoc.superclass();
                if (superclass == null) {
                    return map;
                }
                // 递归并用子类属性替换父类同名的属性
                Map<String, FieldDoc> superMap = getClassFieldDocs(superclass);
                if (superMap.isEmpty()) {
                    return map;
                }
                superMap.putAll(map);
                return superMap;
            });
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<MethodDoc> getNoArgPublicMethodDoc(Class<?> cls, String name) {
        return Optional.ofNullable(getNoArgPublicMethodDocs(cls).get(name));
    }

    public Map<String, MethodDoc> getNoArgPublicMethodDocs(Class<?> cls) {
        return getClassDoc(cls).map(this::getNoArgPublicMethodDocs).orElse(Collections.emptyMap());
    }

    /**
     * 获取无参且公开的方法列表（包含父类）
     *
     * @param classDoc 类文档
     * @return 全限定名 -> 方法的映射
     */
    public Map<String, MethodDoc> getNoArgPublicMethodDocs(ClassDoc classDoc) {
        if (classDoc == null) {
            return Collections.emptyMap();
        }
        try {
            return noArgsMethodDocCache.get(classDoc.typeName(), () -> {
                Map<String, MethodDoc> map = Arrays.stream(classDoc.methods(true)).filter(md -> md.parameters().length == 0).collect(Collectors.toMap(Doc::name, md -> md));
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
            });
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取公开的方法文档（不包括父类）
     *
     * @param classDoc 类文档
     */
    public void addPublicMethodDocs(ClassDoc classDoc) {
        Map<String, MethodDoc> map = Arrays.stream(classDoc.methods(true))
                .collect(Collectors.toMap(md -> StringUtils.replace(md.toString(), " ", ""), md -> md));
        methodDocCache.putAll(map);
    }

    public Optional<MethodDoc> getPublicMethodDoc(String name) {
        return Optional.ofNullable(methodDocCache.get(name));
    }

    /**
     * 获取公开的方法文档（不包括父类）
     *
     * @return 全限定名 -> 方法的映射
     */
    public Map<String, MethodDoc> getPublicMethodDocs() {
        return methodDocCache;
    }

    public Map<String, Object> getProperties() {
        return properties;
    }

    /**
     * 设置属性到上下文 {@link Context}
     *
     * @param key   属性名
     * @param value 属性值
     */
    public void property(String key, Object value) {
        properties.put(key, value);
    }

    public Object getProperty(String key) {
        return properties.get(key);
    }

    public Optional<Class<?>> impl(String source) {
        String impl = implMap.get(source);
        if (impl == null) {
            return Optional.empty();
        }
        try {
            return Optional.ofNullable(Class.forName(impl));
        } catch (ClassNotFoundException e) {
            return Optional.empty();
        }
    }

    public void addEntryClassName(String className) {
        if (StringUtils.isNotBlank(className)) {
            className = StringUtils.substringBefore(className, "[");
            className = StringUtils.substringBefore(className, "<");
            if (StringUtils.equalsAny(className, "void", "int", "long", "float", "double", "byte", "boolean", "char", "short")) {
                return;
            }
            if (StringUtils.startsWithAny(className, "java.lang.", "java.util.")) {
                return;
            }
            entryClassNames.add(className);
        }
    }

    public void addEntryClassName(TypeDesc typeDesc) {
        if (typeDesc != null) {
            addEntryClassName(typeDesc.getType());
            if (typeDesc.getActualParamTypes() != null) {
                for (TypeDesc actualParamType : typeDesc.getActualParamTypes()) {
                    addEntryClassName(actualParamType);
                }
            }
        }
    }
}
