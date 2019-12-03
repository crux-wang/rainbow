package ren.crux.rainbow.core.reader.impl;

import com.sun.javadoc.ProgramElementDoc;
import lombok.NonNull;
import ren.crux.rainbow.core.model.CommentText;
import ren.crux.rainbow.core.model.Describable;
import ren.crux.rainbow.core.model.TagRef;
import ren.crux.rainbow.core.reader.parser.CommonDocParser;
import ren.crux.rainbow.core.reader.parser.Context;
import ren.crux.rainbow.core.reader.parser.TagParser;

import java.util.Optional;

public class DescriptionDocParser implements CommonDocParser<Describable> {

    public static final DescriptionDocParser INSTANCE = new DescriptionDocParser();

    private final TagParser<TagRef> tagRefParser = TagRefParser.INSTANCE;

    @Override
    public Optional<Describable> parse(@NonNull Context context, @NonNull ProgramElementDoc source) {
        Describable desc = new Describable();
        desc.setName(source.name());
        desc.setName(source.qualifiedName());
        CommentText commentText = new CommentText();
        commentText.setText(source.commentText());
        commentText.setInlineTags(tagRefParser.parse(context, source.inlineTags()));
        commentText.setTags(tagRefParser.parse(context, source.tags()));
        desc.setCommentText(commentText);
        return Optional.of(desc);
    }
}
