package ren.crux.rainbow.core;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.RootDoc;
import ren.crux.rainbow.core.entry.Entry;
import ren.crux.rainbow.core.parser.Context;
import ren.crux.rainbow.core.parser.EntryParser;
import ren.crux.rainbow.core.parser.FieldParser;

import java.util.LinkedList;
import java.util.List;

public class JavaDocMain {

    public static void main(String[] args) {
        // 文件路径
        final String path = "/Users/wangzhihui/workspace/project/rainbow/rainbow-core/src/main/java/";
        // 类名
        final String className = "";
        // 执行参数
        final String[] executeParams = JavaDocReader.getExecuteParams(true, path, className);

        // 读取文档
        String javaDcoData = JavaDocReader.readDoc(new JavaDocReader.CallBack() {
            @Override
            public String callback(String path, String className, RootDoc rootDoc, ClassDoc[] classDocs) {
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
                objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
                objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
                List<Entry> entries = new LinkedList<>();
                if (classDocs != null) {
                    FieldParser fieldParser = new FieldParser();
                    for (ClassDoc classDoc : classDocs) {
                        Context context = new Context(classDoc);
                        EntryParser entryParser = new EntryParser();
                        Entry parse = entryParser.parse(context, classDoc);
                        entries.add(parse);
                    }

                }
                try {
                    return objectMapper.writeValueAsString(entries);
                } catch (JsonProcessingException e) {
                    this.error(e);
                    return null;
                }
            }

            @Override
            public void error(Exception e) {
                e.printStackTrace();
            }
        }, path, className, executeParams);

        // 打印文档信息
        System.out.println(javaDcoData);
    }
}
