package ren.crux.rainbow.core.desc.reader.impl;

import com.sun.javadoc.ProgramElementDoc;
import lombok.NonNull;
import ren.crux.rainbow.core.desc.model.CommentText;
import ren.crux.rainbow.core.desc.model.TagRef;
import ren.crux.rainbow.core.desc.reader.parser.CommonDocParser;
import ren.crux.rainbow.core.desc.reader.parser.Context;
import ren.crux.rainbow.core.desc.reader.parser.TagParser;

import java.util.Optional;

public class CommentTextParser implements CommonDocParser<CommentText> {

    public static final CommentTextParser INSTANCE = new CommentTextParser();

    private final TagParser<TagRef> tagRefParser = TagRefParser.INSTANCE;

    @Override
    public Optional<CommentText> parse(@NonNull Context context, @NonNull ProgramElementDoc source) {
        CommentText commentText = new CommentText();
        commentText.setText(source.commentText());
        commentText.setInlineTags(tagRefParser.parse(context, source.inlineTags()));
        commentText.setTags(tagRefParser.parse(context, source.tags()));
        return Optional.of(commentText);
    }
}
