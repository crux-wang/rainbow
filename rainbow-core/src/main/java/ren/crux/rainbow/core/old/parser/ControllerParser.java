package ren.crux.rainbow.core.old.parser;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.MethodDoc;
import ren.crux.rainbow.core.old.model.Requests;
import ren.crux.rainbow.core.parser.Context;
import ren.crux.rainbow.core.parser.JavaDocParser;

/**
 * @author wangzhihui
 */
public class ControllerParser implements JavaDocParser<ClassDoc, Requests> {

    @Override
    public boolean condition(Context context, ClassDoc source) {
        return context.isRestController(source);
    }

    @Override
    public Requests parse(Context context, ClassDoc source) {
        Requests requests = new Requests();
        MethodDoc[] methods = source.methods(true);
        for (MethodDoc method : methods) {
            requests.merge(context.parse(method));
        }
        return requests;
    }
}
