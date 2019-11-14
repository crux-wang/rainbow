package ren.crux.rainbow.core.parser;

import com.sun.javadoc.RootDoc;
import ren.crux.rainbow.core.model.Document;

/**
 * 根解析器
 *
 * @author wangzhihui
 */
public interface RootDocParser extends JavaDocParser<RootDoc, Document> {

    void registerClassDocParser(ClassDocParser classDocParser);

    void unregisterClassDocParser(ClassDocParser classDocParser);

    @Override
    default boolean support(Context context, RootDoc source) {
        return true;
    }

}
