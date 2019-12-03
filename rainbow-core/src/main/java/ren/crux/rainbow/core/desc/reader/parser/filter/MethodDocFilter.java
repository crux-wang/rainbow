package ren.crux.rainbow.core.desc.reader.parser.filter;

import com.sun.javadoc.MethodDoc;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class MethodDocFilter implements DocFilter<MethodDoc> {

    public static final Set<String> IGNORED_METHOD_NAMES = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(
            "toString",
            "equals",
            "hashCode",
            "clone"
    )));

    @Override
    public boolean doFilter(MethodDoc doc) {
        if (!doc.isPublic()) {
            return false;
        }
        String name = doc.name();
        if (StringUtils.startsWith(name, "get") || StringUtils.startsWith(name, "set") || StringUtils.startsWith(name, "is")) {
            return false;
        }
        if (ArrayUtils.isEmpty(doc.annotations())) {
            return false;
        }
        return !IGNORED_METHOD_NAMES.contains(name);
    }
}
