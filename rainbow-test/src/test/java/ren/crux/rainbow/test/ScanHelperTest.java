package ren.crux.rainbow.test;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ren.crux.rainbow.core.DefaultClassDocProvider;
import ren.crux.rainbow.core.DocumentReader;
import ren.crux.rainbow.core.DocumentReaderBuilder;
import ren.crux.rainbow.core.RequestGroupProvider;
import ren.crux.rainbow.core.report.JsonReporter;

@SpringBootTest(classes = TestApplication.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class ScanHelperTest {

    @Autowired
    private RequestGroupProvider requestGroupProvider;
    @Autowired
    private ObjectMapper objectMapper;

    @Before
    public void setUp() throws Exception {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        this.objectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        this.objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
    }

    @Test
    public void name() throws Exception {
        final String path = "/Users/wangzhihui/workspace/project/rainbow/rainbow-test/src/main/java/";
//        final String path = "D:\\workspace\\github\\rainbow\\rainbow-test\\src\\main\\java\\";
        final String[] packageNames = new String[]{"ren.crux.rainbow.test.demo"};
        DefaultClassDocProvider classDocProvider = new DefaultClassDocProvider();
//        classDocProvider.source(path);
//        classDocProvider.packages(packageNames);
//
//        Context context = new Context(classDocProvider);
//        classDocProvider.setUp(context);
//
//        EntryFieldParser entryFieldParser = new EntryFieldParser(AnnotationParser.INSTANCE, CommentTextParser.INSTANCE);
//        EntryMethodParser entryMethodParser = new EntryMethodParser(AnnotationParser.INSTANCE, CommentTextParser.INSTANCE);
//        EntryParser entryParser = new EntryParser(entryFieldParser, entryMethodParser, AnnotationParser.INSTANCE, CommentTextParser.INSTANCE);
//        Pair<Class<?>, ClassDoc> pair = Pair.of(SubTest.class, context.getClassDoc(SubTest.class.getTypeName()).orElse(null));
//        Optional<Entry> optional = entryParser.parse(context, pair);
//        if (optional.isPresent()) {
//            Entry entry = optional.get();
//            System.out.println(objectMapper.writeValueAsString(entry));
//        }

        DocumentReader documentReader = new DocumentReaderBuilder()
                .with(classDocProvider)
                .with(requestGroupProvider)
                .cdp(DefaultClassDocProvider.class)
                .source(path)
                .packages(packageNames)
                .end()
                .impl("org.springframework.data.domain.Page", "org.springframework.data.domain.PageImpl")
                .impl("org.springframework.data.domain.Pageable", "org.springframework.data.domain.PageRequest")
                .useDefaultModule()
                .build();
//        Document document = documentReader.read().orElseThrow(Exception::new);
//        System.out.println(document);
        documentReader.report(JsonReporter.INSTANCE).ifPresent(System.out::println);

//
//        Document document = new DefaultDocumentReader()
//                .with(classDocProvider)
//                .with(requestGroupProvider)
//                .useDefaultModule()
//                .impl("org.springframework.data.domain.Page", "org.springframework.data.domain.PageImpl")
//                .impl("org.springframework.data.domain.Pageable", "org.springframework.data.domain.PageRequest")
//                .cdp()
//                .useDefaultFilter()
//                .source(path)
//                .packages(packageNames)
//                .end()
//                .read().orElse(null);
//
//        Reporter<String> reporter = new HtmlReporter();
//
//        reporter.report(document).ifPresent(html -> {
//            try {
//                File file = new File("test.html");
//                FileUtils.writeStringToFile(file, html, "utf8");
////                Desktop.getDesktop().browse(file.toURI());
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        });
//
//        reporter = new JsonReporter();
//        reporter.report(document).ifPresent(System.out::printf);
    }
}