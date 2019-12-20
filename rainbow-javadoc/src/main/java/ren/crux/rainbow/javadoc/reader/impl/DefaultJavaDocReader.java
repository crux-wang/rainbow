package ren.crux.rainbow.javadoc.reader.impl;

import ren.crux.rainbow.javadoc.model.ClassDesc;
import ren.crux.rainbow.javadoc.reader.AbstractJavaDocReader;
import ren.crux.rainbow.javadoc.reader.ContextConfigurator;
import ren.crux.rainbow.javadoc.reader.parser.RootDocParser;

import java.util.List;

/**
 * 默认 Java 文档阅读器
 *
 * @author wangzhihui
 */
public class DefaultJavaDocReader extends AbstractJavaDocReader<List<ClassDesc>> {

    public DefaultJavaDocReader(RootDocParser<List<ClassDesc>> rootDocParser, ContextConfigurator contextConfigurator) {
        super(rootDocParser, contextConfigurator);
    }

    public DefaultJavaDocReader(RootDocParser<List<ClassDesc>> rootDocParser) {
        super(rootDocParser);
    }

}
