package ren.crux.rainbow.core.output;

import ren.crux.rainbow.core.model.Document;

/**
 * 文档输出
 *
 * @author wangzhihui
 */
public interface DocumentOutput<T> {

    /**
     * 写入
     *
     * @param document 文档
     * @return <T>
     */
    T write(Document document);

}
