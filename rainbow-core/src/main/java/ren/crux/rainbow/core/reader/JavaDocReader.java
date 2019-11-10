package ren.crux.rainbow.core.reader;

import com.sun.javadoc.RootDoc;
import ren.crux.rainbow.core.model.Document;

import java.util.Objects;
import java.util.Optional;

/**
 * @author wangzhihui
 */
public interface JavaDocReader {

    /**
     * 读取
     *
     * @param path         源文件路径
     * @param packageNames 包名列表
     * @return 文档
     */
    Optional<Document> read(String path, String[] packageNames);

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
