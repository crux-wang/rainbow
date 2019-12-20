package ren.crux.rainbow.core.report;

import ren.crux.rainbow.core.model.Document;

import java.util.Optional;

public interface Reporter<T> {

    Optional<T> report(Document document);

}
