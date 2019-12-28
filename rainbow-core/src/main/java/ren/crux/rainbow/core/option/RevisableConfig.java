package ren.crux.rainbow.core.option;

import lombok.NonNull;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 可修改的配置
 *
 * @author wangzhihui
 */
public class RevisableConfig implements Config, ReadOnlyConfig {

    protected Map<Option<?>, Object> options = new ConcurrentHashMap<>();

    /**
     * 设置配置项的值
     *
     * @param option 配置项
     * @param value  给定配置项的值 （ 可以为 {@code null} ）
     * @return 自身
     */
    @Override
    public <T> RevisableConfig setOption(Option<T> option, T value) {
        options.put(option, value);
        return this;
    }

    /**
     * 生成只读配置
     *
     * @return 当前配置的只读版本
     */
    @Override
    public RevisableConfig build() {
        return this;
    }

    /**
     * 获取配置的值
     *
     * @param option 配置项
     * @return 当前配置的值，如果没有配置则返回配置项的默认值，可能是 {@code null}
     */
    @SuppressWarnings("unchecked")
    @Override
    public <T> T getOption(@NonNull Option<T> option) {
        return (T) options.getOrDefault(option, option.getDefaultValue());
    }

    /**
     * 是否含有某配置项
     *
     * @param option 配置项
     * @return
     */
    @Override
    public <T> boolean hasOption(@NonNull Option<T> option) {
        return options.containsKey(option);
    }

    /**
     * 是否为空
     *
     * @return
     */
    @Override
    public boolean isEmpty() {
        return options.isEmpty();
    }


    public Map<String, Object> asMap() {
        Map<String, Object> map = new HashMap<>(options.size());
        options.forEach((o, v) -> map.put(o.getName(), v));
        return map;
    }
}
