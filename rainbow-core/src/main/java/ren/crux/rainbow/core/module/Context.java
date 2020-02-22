package ren.crux.rainbow.core.module;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.SetMultimap;
import com.sun.javadoc.*;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import ren.crux.rainbow.core.ClassDocProvider;
import ren.crux.rainbow.core.option.Option;
import ren.crux.rainbow.core.option.RevisableConfig;
import ren.crux.rainbow.core.utils.EntryUtils;
import ren.crux.raonbow.common.model.TypeDesc;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

/**
 * 上下文
 *
 * @author wangzhhui
 */
@Slf4j
@Getter
public class Context {

    private final RevisableConfig config;
    private final Map<String, String> implMap;
    private final Set<String> entryClassNames = new HashSet<>();
    private final ClassDocProvider classDocProvider;
    private final Cache<String, Map<String, FieldDoc>> fieldDocCache = CacheBuilder.newBuilder().build();
    private final Map<String, MethodDoc> methodDocCache = new HashMap<>();
    private final Cache<String, Map<String, MethodDoc>> noArgsMethodDocCache = CacheBuilder.newBuilder().build();
    private final Map<String, Map<String, ParamTag>> paramTagCache = new HashMap<>();
    private final SetMultimap<String, String> entryFieldClassNames = HashMultimap.create();

    public Context(ClassDocProvider classDocProvider, Map<String, String> implMap) {
        this(new RevisableConfig(), implMap, classDocProvider);
    }

    public Context(RevisableConfig config, Map<String, String> implMap, ClassDocProvider classDocProvider) {
        this.config = config;
        this.implMap = implMap;
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
        map.keySet().forEach(System.out::println);
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

    public RevisableConfig getConfig() {
        return config;
    }

    /**
     * 设置属性到上下文 {@link Context}
     *
     * @param option 选项
     * @param value  值
     */
    public <T> void setOption(Option<T> option, T value) {
        config.setOption(option, value);
    }

    /**
     * 获取选项
     *
     * @param option 选项
     * @param <T>    值类型
     * @return 值
     */
    public <T> T getOption(Option<T> option) {
        return config.getOption(option);
    }

    /**
     * 是否含有某配置项
     *
     * @param option 配置项
     * @return 是否含有某配置项
     */
    public <T> boolean hasOption(@NonNull Option<T> option) {
        return config.hasOption(option);
    }


    public Optional<Class<?>> getImplClass(String source) {
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

    public void addEntryClassName(Collection<String> classNames) {
        EntryUtils.addEntryClassName(entryClassNames, classNames);
    }

    public void addEntryClassName(String className) {
        EntryUtils.addEntryClassName(entryClassNames, className);
    }

    public void addEntryClassName(TypeDesc typeDesc) {
        EntryUtils.addEntryClassName(entryClassNames, typeDesc);
    }

    public void addEntryFieldClassName(String entryType, TypeDesc typeDesc) {
        Set<String> classNames = new HashSet<>();
        EntryUtils.addEntryClassName(classNames, typeDesc);
        EntryUtils.addEntryClassName(entryClassNames, classNames);
        entryFieldClassNames.putAll(entryType, classNames);
    }
}
