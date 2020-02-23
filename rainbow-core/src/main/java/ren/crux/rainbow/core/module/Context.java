package ren.crux.rainbow.core.module;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.SetMultimap;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import ren.crux.rainbow.core.option.Option;
import ren.crux.rainbow.core.option.RevisableConfig;

import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

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
    private final SetMultimap<String, String> entryFieldClassNames = HashMultimap.create();

    public Context(RevisableConfig config, Map<String, String> implMap) {
        this.config = config;
        this.implMap = implMap;
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

}
