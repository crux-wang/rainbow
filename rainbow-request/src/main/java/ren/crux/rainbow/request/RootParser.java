package ren.crux.rainbow.request;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.RootDoc;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import ren.crux.rainbow.core.parser.Context;
import ren.crux.rainbow.core.parser.RootDocParser;
import ren.crux.rainbow.entry.parser.EntryDocParser;
import ren.crux.rainbow.entry.parser.impl.EntryParser;
import ren.crux.rainbow.request.model.RequestGroups;
import ren.crux.rainbow.request.parser.RestControllerDocParser;
import ren.crux.rainbow.request.parser.impl.RestControllerParser;

import java.util.Optional;

@Slf4j
public class RootParser implements RootDocParser<RequestGroups> {

    private final EntryDocParser entryDocParser;
    private final RestControllerDocParser restControllerDocParser;


    public RootParser(EntryDocParser entryDocParser, RestControllerDocParser restControllerDocParser) {
        this.entryDocParser = entryDocParser;
        this.restControllerDocParser = restControllerDocParser;
    }

    public RootParser() {
        this.restControllerDocParser = new RestControllerParser();
        this.entryDocParser = new EntryParser();
    }

    @Override
    public Optional<RequestGroups> parse(@NonNull Context context, @NonNull RootDoc source) {
        RequestGroups groups = new RequestGroups();
        for (ClassDoc classDoc : source.classes()) {
            if (restControllerDocParser.support(context, classDoc)) {
                restControllerDocParser.parse(context, classDoc).ifPresent(groups::addRequestGroup);
            } else if (entryDocParser.support(context, classDoc)) {
                entryDocParser.parse(context, classDoc).ifPresent(groups::addEntry);
            } else {
                log.warn("ignored : {}", classDoc);
            }
        }
        return Optional.of(groups);
    }
}
