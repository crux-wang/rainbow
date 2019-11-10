package ren.crux.rainbow.core.old.parser;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.Tag;
import org.apache.commons.lang3.StringUtils;
import ren.crux.rainbow.core.model.Link;
import ren.crux.rainbow.core.parser.Context;
import ren.crux.rainbow.core.parser.JavaDocParser;

import java.util.*;

public class TagParser implements JavaDocParser<Tag[], List<Link>> {
    @Override
    public boolean condition(Context context, Tag[] source) {
        return true;
    }

    @Override
    public List<Link> parse(Context context, Tag[] source) {
        Set<Link> links = new HashSet<>();
        if (source != null) {
            for (Tag tag : source) {
                Link link = new Link();
                String targetName = tag.text();
                if (StringUtils.equals("@see", tag.kind())) {
                    if (StringUtils.equals("@linkplain", tag.name())) {
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
                    if (StringUtils.equals("Text", tag.name())) {
                        continue;
                    }
                    link.setTarget(targetName);
                }
                link.setTag(tag.name());
                link.setName(targetName);
                links.add(link);
            }
        }
        return new LinkedList<>(links);
    }
}
