package ren.crux.rainbow.core.report;

import ren.crux.rainbow.core.model.Document;

import java.util.Optional;

/**
 * 汇报器
 *
 * @param <T> 输出类型
 */
public interface Reporter<T> {

    /**
     * 汇报
     *
     * @param document 文档
     * @return 汇报结果
     */
    Optional<T> report(Document document);

}
