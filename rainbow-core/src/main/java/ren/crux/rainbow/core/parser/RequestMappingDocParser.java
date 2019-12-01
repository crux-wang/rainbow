package ren.crux.rainbow.core.parser;

import com.sun.javadoc.MethodDoc;
import ren.crux.rainbow.core.model.RequestMapping;
import ren.crux.rainbow.core.reader.parser.JavaDocParser;

import java.util.List;

/**
 * 请求映射文档解析器
 *
 * @author wangzhihui
 */
public interface RequestMappingDocParser extends JavaDocParser<MethodDoc, List<RequestMapping>> {
}
