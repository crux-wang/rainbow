package ren.crux.rainbow.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ren.crux.rainbow.core.report.mock.Mockers;
import ren.crux.rainbow.test.demo.model.Article;

import java.lang.reflect.Type;
import java.util.List;

@SpringBootTest(classes = TestApplication.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class ScanHelperTest {

    @Test
    public void name() throws Exception {

    }

    @Test
    public void name2() {
        Mockers mockers = new Mockers();
        mockers.mock(List.class, new Type[]{Article.class}).ifPresent(System.out::println);
        mockers.mock(Article[].class).ifPresent(System.out::println);
        mockers.mock(Article.class).ifPresent(System.out::println);
    }
}