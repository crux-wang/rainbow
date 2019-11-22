package ren.crux.rainbow.entry;

import com.sun.javadoc.RootDoc;
import lombok.NonNull;
import ren.crux.rainbow.core.parser.Context;
import ren.crux.rainbow.core.parser.RootDocParser;
import ren.crux.rainbow.entry.model.Entry;
import ren.crux.rainbow.entry.parser.EntryDocParser;
import ren.crux.rainbow.entry.parser.impl.EntryParser;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class RootParser implements RootDocParser<Map<String, Entry>> {

    private EntryDocParser entryDocParser;

    public RootParser(EntryDocParser entryDocParser) {
        this.entryDocParser = entryDocParser;
    }

    public RootParser() {
        this.entryDocParser = new EntryParser();
    }

    @Override
    public Optional<Map<String, Entry>> parse(@NonNull Context context, @NonNull RootDoc source) {
        List<Entry> entries = entryDocParser.parse(context, source.classes());
        if (entries.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(entries.stream().collect(Collectors.toMap(Entry::getName, e -> e)));
    }
}
