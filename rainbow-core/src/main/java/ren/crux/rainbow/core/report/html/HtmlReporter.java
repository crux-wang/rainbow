package ren.crux.rainbow.core.report.html;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.apache.commons.lang3.StringUtils;
import ren.crux.rainbow.core.report.Reporter;

import java.io.File;
import java.util.Map;
import java.util.function.Function;

public abstract class HtmlReporter implements Reporter<Map<String, File>> {

    protected static Cache<String, String> cache = CacheBuilder.newBuilder().build();
    protected final String dirPath;
    protected Function<String, String> function;

    protected HtmlReporter(String dirPath) {
        StringUtils.appendIfMissing(dirPath, "/");
        this.dirPath = dirPath;
    }

    public HtmlReporter setTemplateProvider(Function<String, String> function) {
        this.function = function;
        return this;
    }

}
