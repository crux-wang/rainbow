package ren.crux.rainbow.javadoc.reader.parser.filter;

import com.sun.javadoc.FieldDoc;
import org.apache.commons.lang3.StringUtils;

public class DefaultFieldDocFilter implements FieldDocFilter {
    @Override
    public boolean doFilter(FieldDoc doc) {
        return !StringUtils.containsAny(doc.qualifiedName(), "java.lang.Enum.name", "java.lang.Enum.ordinal");
    }
}
