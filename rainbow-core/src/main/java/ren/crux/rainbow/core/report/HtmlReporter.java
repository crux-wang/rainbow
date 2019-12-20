package ren.crux.rainbow.core.report;

import ren.crux.rainbow.core.model.Document;

import java.util.Optional;

public class HtmlReporter implements Reporter<String> {

    @Override
    public Optional<String> report(Document document) {
        return Optional.empty();
    }
}
