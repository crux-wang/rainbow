package ren.crux.rainbow.core.parser;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.FieldDoc;
import com.sun.javadoc.RootDoc;
import com.sun.javadoc.Tag;
import lombok.NonNull;
import ren.crux.rainbow.core.entry.Entry;
import ren.crux.rainbow.core.entry.Link;
import ren.crux.rainbow.core.entry.Tuple;

import java.util.*;
import java.util.concurrent.ExecutionException;

public class Context {

    private final RootDoc rootDoc;
    private final ClassDoc classDoc;
    private final Map<String, Entry> entryMap = new HashMap<>();
    private final FieldParser fieldParser = new FieldParser();
    private final TagParser tagParser = new TagParser();
    private final Cache<String, Link> refCache = CacheBuilder.newBuilder().build();

    public Context(@NonNull RootDoc rootDoc) {
        this.rootDoc = rootDoc;
        ClassDoc[] classDocs = rootDoc.classes();
        Objects.requireNonNull(classDocs);
        if (classDocs.length > 0) {
            this.classDoc = classDocs[0];
        } else {
            this.classDoc = null;
        }
    }

//    private final EntryParser entryParser = new EntryParser();

    public Link getRef(Entry entry) {
        try {
            return refCache.get(entry.getQualifiedName(), () -> {
                Link ref = new Link();
                ref.setName(entry.getName());
                ref.setTarget(entry.getQualifiedName());
                ref.setTag("#Ref");
                return ref;
            });
        } catch (ExecutionException e) {
            return null;
        }
    }

    public void logEntry(Entry entry) {
        entryMap.put(entry.getQualifiedName(), entry);
    }

    public Tuple parse(FieldDoc fieldDoc) {
        return fieldParser.parse(this, fieldDoc);
    }

    public List<Tuple> parse(FieldDoc[] fieldDocs) {
        List<Tuple> fields = new LinkedList<>();
        if (fieldDocs != null) {
            for (FieldDoc fieldDoc : fieldDocs) {
                fields.add(parse(fieldDoc));
            }
        }
        return fields;
    }

    public List<Link> parse(Tag[] tags) {
        return tagParser.parse(this, tags);
    }

    public Optional<Entry> getEntry(String qualifiedName) {
        return Optional.ofNullable(entryMap.get(qualifiedName));
    }

    public Optional<ClassDoc> findClass(String className) {
        return Optional.ofNullable(classDoc).map(cd -> cd.findClass(className));
    }
}
