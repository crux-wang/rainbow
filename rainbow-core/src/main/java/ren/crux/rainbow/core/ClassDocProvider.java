package ren.crux.rainbow.core;

import com.sun.javadoc.ClassDoc;
import lombok.NonNull;
import ren.crux.rainbow.core.module.Context;

import java.util.Optional;

/**
 * 类文档描述提供者
 *
 * @author wangzhihui
 */
public interface ClassDocProvider {

    /**
     * 设置所属的 {@link DocumentReader}
     *
     * @param reader 文档阅读器
     */
    void owner(@NonNull DocumentReaderBuilder reader);

    /**
     * 配置结束
     *
     * @return 所属的 {@link DocumentReaderBuilder}
     */
    @NonNull DocumentReaderBuilder end();

    /**
     * 设置上下文
     *
     * @param context 上下文
     */
    default void setUp(Context context) {
    }

    /**
     * 获取类文档描述
     *
     * @param context   上下文
     * @param className 类名
     * @return 类文档描述
     */
    default Optional<ClassDoc> get(Context context, String className) {
        return Optional.empty();
    }

}
