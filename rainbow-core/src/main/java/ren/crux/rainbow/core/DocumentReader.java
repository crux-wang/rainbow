package ren.crux.rainbow.core;

import ren.crux.rainbow.core.model.Document;
import ren.crux.rainbow.core.report.Reporter;

import java.util.Optional;

/**
 * 文档阅读器
 *
 * @author wangzhihui
 */
public interface DocumentReader {

    /**
     * 读取
     *
     * @return 文档
     */
    Optional<Document> read();

    /**
     * 汇报
     *
     * @param reporter 汇报器
     * @param <T>      汇报结果类型
     * @return 汇报结果
     */
    default <T> Optional<T> report(Reporter<T> reporter) {
        return read().flatMap(reporter::report);
    }

}
