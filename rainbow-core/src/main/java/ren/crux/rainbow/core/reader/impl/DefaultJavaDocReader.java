package ren.crux.rainbow.core.reader.impl;

import ren.crux.rainbow.core.reader.AbstractJavaDocReader;
import ren.crux.rainbow.core.reader.ContextConfigurator;
import ren.crux.rainbow.core.reader.parser.RootDocParser;

/**
 * 默认 Java 文档阅读器
 *
 * @param <T>
 * @author wangzhihui
 */
public class DefaultJavaDocReader<T> extends AbstractJavaDocReader<T> {

    public DefaultJavaDocReader(RootDocParser<T> rootDocParser) {
        super(rootDocParser);
    }

    public DefaultJavaDocReader(RootDocParser<T> rootDocParser, ContextConfigurator contextConfigurator) {
        super(rootDocParser, contextConfigurator);
    }

}
