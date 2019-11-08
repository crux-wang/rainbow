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
import ren.crux.rainbow.core.model.Document;
import ren.crux.rainbow.core.parser.Context;
import ren.crux.rainbow.core.parser.EntryParser;

public class JavaDocMain {

    public static void main(String[] args) throws JsonProcessingException {
        final String path = "D:\\workspace\\github\\rainbow\\rainbow-core\\src\\main\\java\\";
        final String[] packageNames = new String[]{"ren.crux.rainbow.core.test", "xxx"};

        Document document = JavaDocReader.readDoc(path, packageNames, new JavaDocReader.CallBack() {
            @Override
            public Document call(String path, String[] packageNames, RootDoc rootDoc) {
                Document document = new Document();
                if (rootDoc == null) {
                    return document;
                }

                Context context = new Context(rootDoc);
                ClassDoc[] classDocs = rootDoc.classes();
                if (classDocs != null) {
                    for (ClassDoc classDoc : classDocs) {
                        EntryParser entryParser = new EntryParser();
                        Entry parse = entryParser.parse(context, classDoc);
                        document.getEntries().add(parse);
                    }
                }
                return document;
            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();
            }
        });
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        System.out.println(objectMapper.writeValueAsString(document));

    }
}
