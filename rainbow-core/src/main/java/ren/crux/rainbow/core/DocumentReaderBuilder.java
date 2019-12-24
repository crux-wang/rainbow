package ren.crux.rainbow.core;

import com.sun.javadoc.MethodDoc;
import com.sun.javadoc.ParamTag;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import ren.crux.rainbow.core.interceptor.Interceptor;
import ren.crux.rainbow.core.model.EntryMethod;
import ren.crux.rainbow.core.model.RequestParam;
import ren.crux.rainbow.core.module.Context;
import ren.crux.rainbow.core.module.Module;
import ren.crux.rainbow.core.module.ModuleBuilder;

import java.lang.reflect.Method;
import java.util.*;

public class DocumentReaderBuilder {

    protected ClassDocProvider classDocProvider;
    protected RequestGroupProvider requestGroupProvider;
    protected Map<String, Object> properties = new HashMap<>();
    protected Map<String, String> implMap = new HashMap<>();
    protected List<Module> modules = new LinkedList<>();


    /**
     * 设置 {@link ClassDocProvider}
     *
     * @param classDocProvider 类文档提供者
     * @return 自身
     */
    public DocumentReaderBuilder with(ClassDocProvider classDocProvider) {
        this.classDocProvider = classDocProvider;
        this.classDocProvider.owner(this);
        return this;
    }

    /**
     * 设置 {@link RequestGroupProvider}
     *
     * @param requestGroupProvider 请求组提供者
     * @return 自身
     */
    public DocumentReaderBuilder with(RequestGroupProvider requestGroupProvider) {
        this.requestGroupProvider = requestGroupProvider;
        return this;
    }

    /**
     * 设置 {@link ClassDocProvider}
     *
     * @param tClass 实现类
     * @return 自身
     */
    @SuppressWarnings("unchecked")

    public <T extends ClassDocProvider> T cdp(Class<T> tClass) {
        return (T) classDocProvider;
    }

    /**
     * 设置 {@link RequestGroupProvider}
     *
     * @param tClass 实现类
     * @return 自身
     */
    @SuppressWarnings("unchecked")

    public <T extends RequestGroupProvider> T rgp(Class<T> tClass) {
        return (T) requestGroupProvider;
    }

    /**
     * 获取 {@link ClassDocProvider}
     *
     * @return {@link ClassDocProvider}
     */

    public ClassDocProvider cdp() {
        return classDocProvider;
    }

    /**
     * 获取 {@link RequestGroupProvider}
     *
     * @return {@link RequestGroupProvider}
     */

    public RequestGroupProvider rgp() {
        return requestGroupProvider;
    }

    /**
     * 设置属性到上下文 {@link Context}
     *
     * @param key   属性名
     * @param value 属性值
     * @return 自身
     */

    public DocumentReaderBuilder property(String key, Object value) {
        properties.put(key, value);
        return this;
    }

    /**
     * 配置实现类映射
     *
     * @param source 源类名
     * @param impl   实现类类名
     * @return 自身
     */

    public DocumentReaderBuilder impl(String source, String impl) {
        implMap.put(source, impl);
        return this;
    }

    /**
     * 加载外部模块
     *
     * @param modules 模块
     * @return 自身
     */

    public DocumentReaderBuilder modules(Module... modules) {
        if (ArrayUtils.isNotEmpty(modules)) {
            this.modules.addAll(Arrays.asList(modules));
            this.modules.sort(Comparator.comparingInt(Module::order));
        }
        return this;
    }

    /**
     * 使用默认模块
     *
     * @return 自身
     */
    public DocumentReaderBuilder useDefaultModule() {
        ModuleBuilder builder = new ModuleBuilder();
        builder.entryMethod().interceptor(new Interceptor<Pair<Method, MethodDoc>, EntryMethod>() {
            @Override
            public boolean before(Context context, Pair<Method, MethodDoc> source) {
                if (StringUtils.startsWithAny(source.getLeft().getName(), "get", "is")) {
                    return true;
                }
                return false;
            }
        })
                .end()
                .requestParam().interceptor(new Interceptor<Pair<RequestParam, ParamTag>, RequestParam>() {

            @Override
            public boolean before(Context context, Pair<RequestParam, ParamTag> source) {
                if (StringUtils.equals("serialVersionUID", source.getLeft().getName())) {
                    return false;
                }
                return true;
            }
        })
                .end()
                .requestGroup().interceptor(new)
        ;
        modules(builder.build());
        return this;
    }


    /**
     * 构建
     *
     * @return 文档阅读器
     */
    public DocumentReader build() {
        return new DocumentReaderImpl(classDocProvider, requestGroupProvider, properties, implMap, modules);
    }
}
