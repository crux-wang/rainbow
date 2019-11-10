package ren.crux.rainbow.core.reader;

import com.sun.javadoc.RootDoc;
import com.sun.tools.javadoc.Main;
import org.apache.commons.lang3.ArrayUtils;
import ren.crux.rainbow.core.old.model.Document;

import java.util.Optional;

/**
 * @author wangzhihui
 */
public abstract class AbstractJavaDocReader implements JavaDocReader {

    @Override
    public Optional<Document> read(String path, String[] packageNames) {
        return execute(path, packageNames, ArrayUtils.addAll(new String[]{
                "-private", "-doclet", Doclet.class.getName(),
                "-encoding", "utf-8",
                "-sourcepath",
                path,
                "-subpackages"}, packageNames));
    }

    protected Optional<Document> execute(String path, String[] packageNames, String[] args) {
        try {
            Main.execute(args);
            return read0(path, packageNames, Doclet.getRootDoc());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected abstract Optional<Document> read0(String path, String[] packageNames, RootDoc rootDoc);

}
