package ren.crux.rainbow.core;

import ren.crux.rainbow.core.model.Document;
import ren.crux.rainbow.core.report.Reporter;

import java.util.Optional;

/**
 * 文档流
 *
 * @author wangzhihui
 */
public class DocumentStream {

    private Document document;

    public DocumentStream(Document document) {
        this.document = document;
    }

    public Optional<Document> get() {
        return Optional.ofNullable(document);
    }

    public <T> Optional<T> report(Reporter<T> reporter) {
        return get().flatMap(reporter::report);
    }

}
