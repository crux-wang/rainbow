package ren.crux.rainbow.core.parser;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.RootDoc;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import ren.crux.rainbow.core.model.Document;
import ren.crux.rainbow.core.reader.parser.Context;
import ren.crux.rainbow.core.reader.parser.RootDocParser;

import java.util.Optional;
import java.util.Set;

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
    public Optional<Document> parse0(@NonNull Context context, @NonNull RootDoc source) {
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
        Set<String> types = context.getTypes();
        if (!types.isEmpty()) {
            for (String type : types) {
                if (StringUtils.startsWith(type, "java.lang.")) {
                    continue;
                }
                if (!document.getEntryMap().containsKey(type)) {
                    context.findClass(type)
                            .filter(classDoc -> !classDoc.isAbstract() && classDoc.isClass())
                            .flatMap(classDoc -> context.getEntryDocParser().parse(context, classDoc))
                            .ifPresent(document::addEntry);
                }
            }
        }
        return Optional.of(document);
    }
}
