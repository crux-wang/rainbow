package ren.crux.rainbow.core.module.filter;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import ren.crux.rainbow.core.Context;

public class DefaultEntryClassNameFilter implements EntryClassNameFilter {

    private String[] match = new String[0];
    private String[] prefix = new String[0];
    private String[] suffix = new String[0];
    private String[] contains = new String[0];

    public DefaultEntryClassNameFilter contains(String... contains) {
        if (contains != null) {
            if (this.contains == null) {
                this.contains = contains;
            } else {
                this.contains = ArrayUtils.addAll(this.contains, contains);
            }
        }
        return this;
    }

    public DefaultEntryClassNameFilter match(String... classNames) {
        if (classNames != null) {
            if (this.match == null) {
                this.match = classNames;
            } else {
                this.match = ArrayUtils.addAll(this.match, classNames);
            }
        }
        return this;
    }

    public DefaultEntryClassNameFilter starts(String... prefix) {
        if (prefix != null) {
            if (this.prefix == null) {
                this.prefix = prefix;
            } else {
                this.prefix = ArrayUtils.addAll(this.prefix, prefix);
            }
        }
        return this;
    }

    public DefaultEntryClassNameFilter ends(String... suffix) {
        if (suffix != null) {
            if (this.suffix == null) {
                this.suffix = suffix;
            } else {
                this.suffix = ArrayUtils.addAll(this.suffix, suffix);
            }
        }
        return this;
    }

    public DefaultEntryClassNameFilter useDefault() {
        ends("Util", "Utils", "Helper", "Service", "Controller", "Interceptor");
        starts("java.lang.", "java.util.");
        match("javax.servlet.http.HttpServletRequest", "javax.servlet.http.HttpServletResponse");
        return this;
    }

    @Override
    public boolean doFilter(Context context, String className) {
        if (StringUtils.equalsAny(className, match)) {
            return false;
        }
        if (StringUtils.containsAny(className, contains)) {
            return false;
        }
        if (StringUtils.startsWithAny(className, prefix)) {
            return false;
        }
        if (StringUtils.equalsAny(className, suffix)) {
            return false;
        }
        return true;
    }
}
