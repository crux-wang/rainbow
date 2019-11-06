package ren.crux.rainbow.core.parser;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.FieldDoc;
import com.sun.javadoc.Tag;
import ren.crux.rainbow.core.entry.Entry;
import ren.crux.rainbow.core.entry.Link;
import ren.crux.rainbow.core.entry.Tuple;

import java.util.*;

public class Context {

    private final ClassDoc classDoc;
    private final Map<String, Entry> entryMap = new HashMap<>();
    private final FieldParser fieldParser = new FieldParser();
    private final TagParser tagParser = new TagParser();

    public Context(ClassDoc classDoc) {
        this.classDoc = classDoc;
    }

//    private final EntryParser entryParser = new EntryParser();

    public void logEntry(Entry entry) {
        System.out.println("log entry : " + entry.getQualifiedName());
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
        return Optional.ofNullable(classDoc.findClass(className));
    }
}
