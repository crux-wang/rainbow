package ren.crux.rainbow.entry.parser.impl;

import com.sun.javadoc.ProgramElementDoc;
import lombok.NonNull;
import ren.crux.rainbow.core.parser.Context;
import ren.crux.rainbow.entry.model.Description;
import ren.crux.rainbow.entry.parser.DescriptionDocParser;
import ren.crux.rainbow.entry.parser.RefDocParser;

import java.util.Optional;

public class DescriptionParser implements DescriptionDocParser {

    private final RefDocParser refDocParser;

    public DescriptionParser(RefDocParser refDocParser) {
        this.refDocParser = refDocParser;
    }

    public DescriptionParser() {
        this.refDocParser = new RefParser();
    }

    @Override
    public Optional<Description> parse(@NonNull Context context, @NonNull ProgramElementDoc source) {
        Description desc = new Description();
        desc.setCommentText(source.commentText());
        desc.setInlineRefs(refDocParser.parse(context, source.inlineTags()));
        desc.setRefs(refDocParser.parse(context, source.tags()));
        return Optional.of(desc);
    }
}
