package ren.crux.rainbow.core.parser;

import com.sun.javadoc.RootDoc;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * @author wangzhihui
 */
public interface Context {

    /**
     * 获取根文档
     *
     * @return 根文档
     */
    @NonNull
    RootDoc getRootDoc();

}
