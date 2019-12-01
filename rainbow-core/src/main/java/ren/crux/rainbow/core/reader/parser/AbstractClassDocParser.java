package ren.crux.rainbow.core.reader.parser;

import com.sun.javadoc.ClassDoc;
import lombok.NonNull;

/**
 * 抽象类文档解析器
 *
 * @author wangzhihui
 */
public abstract class AbstractClassDocParser<T> implements ClassDocParser<T> {

    /**
     * 支持条件
     *
     * @param context 上下文
     * @param source  解析源
     * @return 是否支持
     */
    @Override
    public boolean support(@NonNull Context context, @NonNull ClassDoc source) {
        return source.isPublic() && source.isClass();
    }

}
