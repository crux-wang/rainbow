package ren.crux.rainbow.core.old.parser;

import com.sun.javadoc.ClassDoc;
import ren.crux.rainbow.core.old.model.Document;
import ren.crux.rainbow.core.parser.Context;
import ren.crux.rainbow.core.parser.JavaDocParser;

/**
 * @author wangzhihui
 */
public class ClassParser implements JavaDocParser<ClassDoc, Document> {

    @Override
    public boolean condition(Context context, ClassDoc source) {
        return source.isClass();
    }

    @Override
    public Document parse(Context context, ClassDoc source) {
        Document document = new Document();
        if (context.isRestController(source)) {
            document.addItem(context.parseController(source));
        } else if (context.entryCondition(source)) {
            document.addEntry(context.parseEntry(source));
        } else {
            System.out.println("ignored " + source);
        }
        return document;
    }
}
