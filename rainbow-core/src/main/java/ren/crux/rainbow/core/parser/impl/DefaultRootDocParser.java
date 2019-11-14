package ren.crux.rainbow.core.parser.impl;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.RootDoc;
import lombok.extern.slf4j.Slf4j;
import ren.crux.rainbow.core.model.Document;
import ren.crux.rainbow.core.parser.ClassDocParser;
import ren.crux.rainbow.core.parser.Context;
import ren.crux.rainbow.core.parser.RootDocParser;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

/**
 * @author wangzhihui
 */
@Slf4j
public class DefaultRootDocParser implements RootDocParser {

    private List<ClassDocParser> classDocParsers = new LinkedList<>();
    /**
     * 解析
     *
     * @param context 上下文
     * @param source  解析源
     * @return 解析后的产物
     */
    @Override
    public Optional<Document> parse(Context context, RootDoc source) {
        final Document document = new Document();
        ClassDoc[] classDocs = source.classes();
        log.debug("parser classes : {}", classDocs.length);
        for (ClassDoc classDoc : classDocs) {
            classDocParsers.forEach(p -> p.parseAndMerge(context, classDoc, document));
        }
        return Optional.of(document);
    }

    @Override
    public void registerClassDocParser(ClassDocParser classDocParser) {
        classDocParsers.add(classDocParser);
    }

    @Override
    public void unregisterClassDocParser(ClassDocParser classDocParser) {
        classDocParsers.remove(classDocParser);
    }
}
