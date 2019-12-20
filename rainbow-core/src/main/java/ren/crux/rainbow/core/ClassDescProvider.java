package ren.crux.rainbow.core;

import ren.crux.rainbow.javadoc.model.ClassDesc;
import ren.crux.rainbow.javadoc.reader.parser.filter.*;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

public interface ClassDescProvider {

    ClassDescProvider EMPTY = new ClassDescProvider() {

    };

    default void owner(DocumentReader reader) {

    }

    default DocumentReader end() {
        return null;
    }

    default ClassDescProvider source(String sourcePath) {
        return this;
    }

    default ClassDescProvider packages(String... packages) {
        return this;
    }

    default ClassDescProvider filter(ClassDocFilter... filters) {
        return this;
    }

    default ClassDescProvider filter(FieldDocFilter... filters) {
        return this;
    }

    default ClassDescProvider filter(MethodDocFilter... filters) {
        return this;
    }

    default ClassDescProvider useDefaultFilter() {
        filter(new DefaultClassDocFilter());
        filter(new DefaultMethodDocFilter());
        filter(new DefaultFieldDocFilter());
        return this;
    }

    default void setUp(Context context) {
    }

    default Map<String, ClassDesc> all() {
        return Collections.emptyMap();
    }

    default Optional<ClassDesc> get(Context context, String className) {
        return Optional.empty();
    }

}
