package ren.crux.rainbow.core;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.RootDoc;
import ren.crux.rainbow.core.docs.JavaDocReader;
import ren.crux.rainbow.core.entry.Entry;
import ren.crux.rainbow.core.parser.Context;
import ren.crux.rainbow.core.parser.EntryParser;
import ren.crux.rainbow.core.parser.FieldParser;

import java.util.LinkedList;
import java.util.List;

public class JavaDocMain {

    public static void main(String[] args) {
        final String path = "D:\\workspace\\github\\rainbow\\rainbow-core\\src\\main\\java\\";
        final String packageName = "ren.crux.rainbow.core.test";
        String javaDcoData = JavaDocReader.readDoc(path, packageName, new JavaDocReader.CallBack() {
            @Override
            public String call(String path, String packageName, RootDoc rootDoc) {
                if (rootDoc == null) {
                    return null;
                }
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
                objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
                objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
                List<Entry> entries = new LinkedList<>();
                Context context = new Context(rootDoc);
                ClassDoc[] classDocs = rootDoc.classes();
                if (classDocs != null) {
                    FieldParser fieldParser = new FieldParser();
                    for (ClassDoc classDoc : classDocs) {
                        EntryParser entryParser = new EntryParser();
                        Entry parse = entryParser.parse(context, classDoc);
                        entries.add(parse);
                    }

                }
                try {
                    return objectMapper.writeValueAsString(entries);
                } catch (JsonProcessingException e) {
                    this.onError(e);
                    return null;
                }
            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();
            }
        });
        System.out.println(javaDcoData);
    }
}
