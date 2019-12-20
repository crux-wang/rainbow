package ren.crux.rainbow.core;

import ren.crux.rainbow.core.model.Document;
import ren.crux.rainbow.core.module.Module;

import java.util.Optional;

public interface DocumentReader {

    DocumentReader with(ClassDescProvider classDescProvider);

    DocumentReader with(RequestGroupProvider requestGroupProvider);

    <T extends ClassDescProvider> T cdp(Class<T> tClass);

    <T extends RequestGroupProvider> T rgp(Class<T> tClass);

    ClassDescProvider cdp();

    RequestGroupProvider rgp();

    DocumentReader option(String key, Object value);

    DocumentReader modules(Module... modules);

    DocumentReader useDefaultModule();

    Optional<Document> read();

}
