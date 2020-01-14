package ren.crux.rainbow.javadoc.reader;

import com.sun.javadoc.RootDoc;

import java.util.Objects;
import java.util.Optional;

/**
 * Java 文档读取器
 *
 * @author wangzhihui
 */
public interface JavaDocReader<T> {

    /**
     * 读取
     *
     * @param path         源文件路径
     * @param packageNames 包名列表
     * @return T
     */
    Optional<T> read(String[] path, String[] packageNames);

    public static class Doclet {

        private static RootDoc rootDoc;

        public static boolean start(final RootDoc rootDoc) {
            Doclet.rootDoc = rootDoc;
            return true;
        }

        public static RootDoc getRootDoc() {
            return Objects.requireNonNull(rootDoc);
        }
    }

}
