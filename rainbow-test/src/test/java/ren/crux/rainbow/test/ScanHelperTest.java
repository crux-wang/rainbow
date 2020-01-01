package ren.crux.rainbow.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ren.crux.rainbow.core.DefaultClassDocProvider;
import ren.crux.rainbow.core.DocumentReader;
import ren.crux.rainbow.core.DocumentReaderBuilder;
import ren.crux.rainbow.core.RequestGroupProvider;
import ren.crux.rainbow.core.module.ParserOptionModule;
import ren.crux.rainbow.core.report.MarkdownReport;
import ren.crux.rainbow.runtime.SpringWebModule;

import static ren.crux.rainbow.core.module.ParserOptionModule.IgnoredOption.signature;
import static ren.crux.rainbow.core.module.ParserOptionModule.IgnoredOption.tags;

@SpringBootTest(classes = TestApplication.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class ScanHelperTest {

    @Autowired
    private RequestGroupProvider requestGroupProvider;

    @Test
    public void name() throws Exception {
//        final String path = "/Users/wangzhihui/workspace/project/rainbow/rainbow-test/src/main/java/";
//        final String path2 = "/Users/wangzhihui/workspace/project/rainbow/rainbow-core/src/main/java/";
//        final String path3 = "/Users/wangzhihui/workspace/project/rainbow/rainbow-javadoc/src/main/java/";
        final String path = "D:\\workspace\\github\\rainbow\\rainbow-test\\src\\main\\java\\";
        final String[] packageNames = new String[]{"ren.crux.rainbow"};
        DefaultClassDocProvider classDocProvider = new DefaultClassDocProvider();
        DocumentReader documentReader = new DocumentReaderBuilder()
                .with(classDocProvider)
                .with(requestGroupProvider)
                .cdp(DefaultClassDocProvider.class)
//                .source(path, path2, path3)
                .source(path)
                .packages(packageNames)
                .end()
                .option(ParserOptionModule.IGNORED_OPTION, new ParserOptionModule.IgnoredOption[]{tags, signature})
                .useDefaultModule()
                .modules(SpringWebModule.INSTANCE)
                .build();


//        documentReader.read().report(JsonReporter.INSTANCE).ifPresent(html -> {
//            try {
//                File file = new File("test.json");
//                FileUtils.writeStringToFile(file, html, "utf8");
////                Desktop.getDesktop().browse(file.toURI());
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        });

        documentReader.read().report(MarkdownReport.INSTANCE).ifPresent(System.out::println);
    }
}