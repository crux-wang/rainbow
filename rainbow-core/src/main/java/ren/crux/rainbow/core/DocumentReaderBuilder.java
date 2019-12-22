package ren.crux.rainbow.core;

import ren.crux.rainbow.common.Module;

/**
 * @author wangzhihui
 */
public interface DocumentReaderBuilder {

    /**
     * 设置 {@link ClassDocProvider}
     *
     * @param classDocProvider 类文档提供者
     * @return 自身
     */
    DocumentReaderBuilder with(ClassDocProvider classDocProvider);

    /**
     * 设置 {@link RequestGroupProvider}
     *
     * @param requestGroupProvider 请求组提供者
     * @return 自身
     */
    DocumentReaderBuilder with(RequestGroupProvider requestGroupProvider);

    /**
     * 设置 {@link ClassDocProvider}
     *
     * @param tClass 实现类
     * @param <T>    实现类类型
     * @return 自身
     */
    <T extends ClassDocProvider> T cdp(Class<T> tClass);

    /**
     * 设置 {@link RequestGroupProvider}
     *
     * @param tClass 实现类
     * @param <T>    实现类类型
     * @return 自身
     */
    <T extends RequestGroupProvider> T rgp(Class<T> tClass);

    /**
     * 获取 {@link ClassDocProvider}
     *
     * @return {@link ClassDocProvider}
     */
    ClassDocProvider cdp();

    /**
     * 获取 {@link RequestGroupProvider}
     *
     * @return {@link RequestGroupProvider}
     */
    RequestGroupProvider rgp();

    /**
     * 设置属性到上下文 {@link Context}
     *
     * @param key   属性名
     * @param value 属性值
     * @return 自身
     */
    DocumentReaderBuilder property(String key, Object value);

    /**
     * 配置实现类映射
     *
     * @param source 源类名
     * @param impl   实现类类名
     * @return 自身
     */
    DocumentReaderBuilder impl(String source, String impl);

    /**
     * 加载外部模块
     *
     * @param modules 模块
     * @return 自身
     */
    DocumentReaderBuilder modules(Module... modules);

    /**
     * 使用默认模块
     *
     * @return
     */
    DocumentReaderBuilder useDefaultModule();

    /**
     * 构建
     *
     * @return 文档阅读器
     */
    DocumentReader build();

}
