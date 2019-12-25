package ren.crux.rainbow.javadoc.reader;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.RootDoc;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author wangzhihui
 */
public class DefaultJavaDocReader extends AbstractJavaDocReader<List<ClassDoc>> {
    /**
     * 实际读取方法
     *
     * @param path         源文件路径
     * @param packageNames 包名列表
     * @param rootDoc      根文档
     * @return T
     */
    @Override
    protected Optional<List<ClassDoc>> read0(String[] path, String[] packageNames, RootDoc rootDoc) {
        ClassDoc[] classes = rootDoc.classes();
        if (classes != null) {
            return Optional.of(Arrays.stream(classes).collect(Collectors.toList()));
        }
        return Optional.empty();
    }
}
