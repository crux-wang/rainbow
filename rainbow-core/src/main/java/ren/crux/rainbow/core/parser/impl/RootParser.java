package ren.crux.rainbow.core.parser.impl;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.RootDoc;
import lombok.extern.slf4j.Slf4j;
import ren.crux.rainbow.core.model.Document;
import ren.crux.rainbow.core.parser.Context;
import ren.crux.rainbow.core.parser.RootDocParser;

/**
 * @author wangzhihui
 */
@Slf4j
public class RootParser implements RootDocParser {
    /**
     * 解析
     *
     * @param context 上下文
     * @param source  解析源
     * @return 解析后的产物
     */
    @Override
    public Document parse(Context context, RootDoc source) {
        Document document = new Document();
        ClassDoc[] classes = source.classes();
        log.debug("parser classes : {}", classes.length);
        for (ClassDoc classDoc : classes) {
            if (context.isRestController(classDoc)) {
                log.debug("parser RestController");
                context.getRestControllerParser().ifPresent(p -> {
                    document.addItem(p.parse(context, classDoc));
                });
            } else if (context.isEntry(classDoc)) {
                log.debug("parser Entry");
                context.getEntryDocParser().ifPresent(p -> {
                    document.addEntry(p.parse(context, classDoc));
                });
            } else {
                log.warn("ignored parser : {}", classDoc);
            }
        }
        return document;
    }
}
