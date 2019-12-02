package ren.crux.rainbow.core.parser.impl;

import com.sun.javadoc.ProgramElementDoc;
import lombok.NonNull;
import ren.crux.rainbow.core.model.Description;
import ren.crux.rainbow.core.parser.DescriptionDocParser;
import ren.crux.rainbow.core.reader.parser.Context;

import java.util.Optional;

/**
 * 默认描述文档解析器
 *
 * @author wangzhihui
 */
public class DefaultDescriptionDocParser implements DescriptionDocParser {
    @Override
    public Optional<Description> parse0(@NonNull Context context, @NonNull ProgramElementDoc source) {
        Description desc = new Description();
        desc.setCommentText(source.commentText());
        desc.setInlineRefs(context.getRefDocParser().parse(context, source.inlineTags()));
        desc.setRefs(context.getRefDocParser().parse(context, source.tags()));
        return Optional.of(desc);
    }
}
