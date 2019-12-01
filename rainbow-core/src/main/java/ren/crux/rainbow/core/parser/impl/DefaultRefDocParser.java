package ren.crux.rainbow.core.parser.impl;

import com.sun.javadoc.Tag;
import lombok.NonNull;
import ren.crux.rainbow.core.model.Ref;
import ren.crux.rainbow.core.parser.RefDocParser;
import ren.crux.rainbow.core.reader.parser.Context;

import java.util.Optional;

/**
 * 默认引用文档解析器
 *
 * @author wangzhihui
 */
public class DefaultRefDocParser implements RefDocParser {

    @Override
    public Optional<Ref> parse(@NonNull Context context, @NonNull Tag source) {
        if ("Text".equals(source.name())) {
            return Optional.empty();
        }
        Ref ref = new Ref();
        ref.setName(source.name());
        ref.setText(source.text());
        context.findClass(source.text()).ifPresent(classDoc -> ref.setTarget(classDoc.qualifiedName()));
        return Optional.of(ref);
    }

}
