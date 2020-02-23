package ren.crux.rainbow.core;

import lombok.NonNull;
import org.apache.commons.lang3.ArrayUtils;
import ren.crux.rainbow.core.module.Context;
import ren.crux.rainbow.core.module.Module;
import ren.crux.rainbow.core.option.Option;
import ren.crux.rainbow.core.option.RevisableConfig;

import java.util.*;

/**
 * @author wangzhihui
 */
public class DocumentReaderBuilder {

    private final RevisableConfig config = new RevisableConfig();
    private Map<String, String> implMap = new HashMap<>();
    private List<Module> modules = new LinkedList<>();
    private QdoxBuilder qdoxBuilder = new QdoxBuilder(this);

    /**
     * 配置输入信息
     *
     * @return qdox 构建器
     */
    public QdoxBuilder input() {
        return qdoxBuilder;
    }

    /**
     * 设置属性到上下文 {@link Context}
     *
     * @param option 属性名
     * @param value  属性值
     * @return 自身
     */
    public <T> DocumentReaderBuilder option(@NonNull Option<T> option, T value) {
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
    public DocumentReaderBuilder impl(@NonNull String source, @NonNull String impl) {
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

    public DocumentReader build() {
        return new DocumentReader(qdoxBuilder.build(), config, implMap, modules);
    }
}
