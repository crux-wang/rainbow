package ren.crux.rainbow.entry.parser.impl;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.Tag;
import org.apache.commons.lang3.StringUtils;
import ren.crux.rainbow.core.model.Link;
import ren.crux.rainbow.core.parser.Context;
import ren.crux.rainbow.entry.parser.TagDocParser;

import java.util.Optional;

/**
 * @author wangzhihui
 */
public class TagParser implements TagDocParser {
    /**
     * 解析
     *
     * @param context 上下文
     * @param source  解析源
     * @return 解析后的产物
     */
    @Override
    public Optional<Link> parse(Context context, Tag source) {
        Link link = new Link();
        String targetName = source.text();
        if (StringUtils.equals("@see", source.kind())) {
            if (StringUtils.equals("@linkplain", source.name())) {
                targetName = StringUtils.split(targetName, " ")[0];
            }
            Optional<ClassDoc> optional = context.findClass(targetName);
            if (optional.isPresent()) {
                link.setTarget(optional.get().qualifiedName());
            } else {
                link.setTarget(targetName);
                link.setUnknown(true);
            }
        } else {
            if (StringUtils.equals("Text", source.name())) {
                return Optional.empty();
            }
        }
        link.setTag(source.name());
        link.setName(targetName);
        return Optional.of(link);
    }
}
