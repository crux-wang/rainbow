package ren.crux.rainbow.core.reader.impl;

import com.sun.javadoc.RootDoc;
import ren.crux.rainbow.core.reader.AbstractJavaDocReader;

import java.util.Optional;

/**
 * @author wangzhihui
 */
public class DefaultJavaDocReader extends AbstractJavaDocReader<RootDoc> {

    @Override
    protected Optional<RootDoc> read0(String path, String[] packageNames, RootDoc rootDoc) {
        return Optional.of(rootDoc);
    }

}
