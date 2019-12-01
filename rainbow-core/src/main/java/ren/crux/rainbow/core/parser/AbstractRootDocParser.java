package ren.crux.rainbow.core.parser;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.RootDoc;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import ren.crux.rainbow.core.model.Document;
import ren.crux.rainbow.core.reader.parser.Context;
import ren.crux.rainbow.core.reader.parser.RootDocParser;

import java.util.Optional;

/**
 * 抽象根文档解析器
 *
 * @author wangzhihui
 */
@Slf4j
public abstract class AbstractRootDocParser implements RootDocParser<Document> {

    /**
     * 解析
     *
     * @param context 上下文
     * @param source  解析源
     * @return 解析后的产物
     */
    @Override
    public Optional<Document> parse(@NonNull Context context, @NonNull RootDoc source) {
        Document document = new Document();
        for (ClassDoc classDoc : source.classes()) {
            if (context.getRequestGroupDocParser().support(context, classDoc)) {
                context.getRequestGroupDocParser().parse(context, classDoc).ifPresent(document::addRequestGroup);
            } else if (context.getEntryDocParser().support(context, classDoc)) {
                context.getEntryDocParser().parse(context, classDoc).ifPresent(document::addEntry);
            } else {
                log.warn("ignored : {}", classDoc);
            }
        }
        return Optional.of(document);
    }
}
