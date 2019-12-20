package ren.crux.rainbow.javadoc.reader.parser.filter;

import com.sun.javadoc.ClassDoc;
import org.apache.commons.lang3.StringUtils;

public class DefaultClassDocFilter implements ClassDocFilter {

    @Override
    public boolean doFilter(ClassDoc doc) {
        String name = doc.name();
        if (StringUtils.endsWithAny(name, "Util", "Utils", "Helper", "Service", "Interceptor")) {
            return false;
        }
        if (doc.qualifiedName().startsWith("java.lang.")) {
            return false;
        }
        if (doc.qualifiedName().startsWith("java.util.")) {
            return false;
        }
        return true;
    }
}
