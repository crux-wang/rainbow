package ren.crux.rainbow.core;

import org.apache.commons.lang3.ArrayUtils;
import ren.crux.rainbow.core.module.Context;
import ren.crux.rainbow.core.module.DefaultModule;
import ren.crux.rainbow.core.module.Module;
import ren.crux.rainbow.core.module.ParserOptionModule;
import ren.crux.rainbow.core.option.Option;
import ren.crux.rainbow.core.option.RevisableConfig;

import java.util.*;

/**
 * @author wangzhihui
 */
public class DocumentReaderBuilder {

    protected ClassDocProvider classDocProvider;
    protected RequestGroupProvider requestGroupProvider;
    private final RevisableConfig config = new RevisableConfig();
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
     * @param option 属性名
     * @param value  属性值
     * @return 自身
     */
    public <T> DocumentReaderBuilder option(Option<T> option, T value) {
        config.setOption(option, value);
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
        modules(DefaultModule.INSTANCE, ParserOptionModule.INSTANCE);
//        impl("org.springframework.data.domain.Page", "org.springframework.data.domain.PageImpl");
//        impl("org.springframework.data.domain.Pageable", "org.springframework.data.domain.PageRequest");
        return this;
    }


    /**
     * 构建
     *
     * @return 文档阅读器
     */
    public DocumentReader build() {
        return new DocumentReaderImpl(classDocProvider, requestGroupProvider, config, implMap, modules);
    }
}
