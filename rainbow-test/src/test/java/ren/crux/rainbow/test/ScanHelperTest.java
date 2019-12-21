package ren.crux.rainbow.test;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ren.crux.rainbow.core.ClassDescProvider;
import ren.crux.rainbow.core.DefaultClassDescProvider;
import ren.crux.rainbow.core.DefaultDocumentReader;
import ren.crux.rainbow.core.RequestGroupProvider;
import ren.crux.rainbow.core.model.Document;
import ren.crux.rainbow.core.report.HtmlReporter;
import ren.crux.rainbow.core.report.Reporter;

import java.io.File;
import java.io.IOException;

@SpringBootTest(classes = TestApplication.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class ScanHelperTest {

    @Autowired
    private RequestGroupProvider requestGroupProvider;

    @Test
    public void name() throws Exception {
//        final String path = "/Users/wangzhihui/workspace/project/rainbow/rainbow-test/src/main/java/";
        final String path = "D:\\workspace\\github\\rainbow\\rainbow-test\\src\\main\\java\\";
        final String[] packageNames = new String[]{"ren.crux.rainbow.test.demo"};
        ClassDescProvider classDescProvider = new DefaultClassDescProvider();

        Document document = new DefaultDocumentReader()
                .with(classDescProvider)
                .with(requestGroupProvider)
                .useDefaultModule()
                .impl("org.springframework.data.domain.Page", "org.springframework.data.domain.PageImpl")
                .impl("org.springframework.data.domain.Pageable", "org.springframework.data.domain.PageRequest")
                .cdp()
                .useDefaultFilter()
                .source(path)
                .packages(packageNames)
                .end()
                .read().orElse(null);

        Reporter<String> reporter = new HtmlReporter();
        reporter.report(document).ifPresent(html -> {
            try {
                File file = new File("test.html");
                FileUtils.writeStringToFile(file, html, "utf8");
//                Desktop.getDesktop().browse(file.toURI());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });


    }
}