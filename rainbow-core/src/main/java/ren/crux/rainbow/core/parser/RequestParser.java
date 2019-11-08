package ren.crux.rainbow.core.parser;

import com.sun.javadoc.MethodDoc;
import ren.crux.rainbow.core.model.Request;

/**
 * @author wangzhihui
 */
public class RequestParser implements Parser<MethodDoc, Request> {

    @Override
    public boolean condition(Context context, MethodDoc source) {
        return source.isMethod() && context.isRequest(source);
    }

    @Override
    public Request parse(Context context, MethodDoc source) {
        return null;
    }
}
