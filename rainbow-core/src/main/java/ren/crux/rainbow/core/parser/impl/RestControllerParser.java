package ren.crux.rainbow.core.parser.impl;

import com.sun.javadoc.ClassDoc;
import ren.crux.rainbow.core.model.RequestItem;
import ren.crux.rainbow.core.parser.Context;
import ren.crux.rainbow.core.parser.RequestItemDocParser;

import java.util.Optional;

/**
 * @author wangzhihui
 */
public class RestControllerParser implements RequestItemDocParser {
    @Override
    public Optional<RequestItem> parse(Context context, ClassDoc source) {
        return Optional.empty();
    }
}
