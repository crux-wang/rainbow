package ren.crux.rainbow.core.reader;

import com.sun.javadoc.RootDoc;
import com.sun.tools.javadoc.Main;
import org.apache.commons.lang3.ArrayUtils;
import ren.crux.rainbow.core.reader.parser.Context;
import ren.crux.rainbow.core.reader.parser.RootDocParser;

import java.util.Optional;

/**
 * 基本 Java 文档读取器
 *
 * @author wangzhihui
 */
public abstract class AbstractJavaDocReader<T> implements JavaDocReader<T> {

    protected final RootDocParser<T> rootDocParser;
    private final ContextConfigurator contextConfigurator;

    protected AbstractJavaDocReader(RootDocParser<T> rootDocParser, ContextConfigurator contextConfigurator) {
        this.rootDocParser = rootDocParser;
        this.contextConfigurator = contextConfigurator;
    }

    protected AbstractJavaDocReader(RootDocParser<T> rootDocParser) {
        this.rootDocParser = rootDocParser;
        this.contextConfigurator = new ContextConfigurator() {
        };
    }

    /**
     * 读取
     *
     * @param path         源文件路径
     * @param packageNames 包名列表
     * @return T
     */
    @Override
    public Optional<T> read(String path, String[] packageNames) {
        return execute(path, packageNames, ArrayUtils.addAll(new String[]{
                "-private", "-doclet", Doclet.class.getName(),
                "-encoding", "utf-8",
                "-sourcepath",
                path,
                "-subpackages"}, packageNames));
    }

    private Optional<T> execute(String path, String[] packageNames, String[] args) {
        try {
            Main.execute(args);
            return read0(path, packageNames, Doclet.getRootDoc());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 实际读取方法
     *
     * @param path         源文件路径
     * @param packageNames 包名列表
     * @param rootDoc      根文档
     * @return T
     */
    private Optional<T> read0(String path, String[] packageNames, RootDoc rootDoc) {
        return rootDocParser.parse(Context.newContext(rootDoc, path, packageNames), rootDoc);
    }

}
