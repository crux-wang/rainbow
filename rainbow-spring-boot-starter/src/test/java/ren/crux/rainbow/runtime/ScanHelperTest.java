package ren.crux.rainbow.runtime;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@SpringBootTest(classes = TestApplication.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class ScanHelperTest {

    @Autowired
    private ScanHelper scanHelper;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void name() throws JsonProcessingException {
        System.out.println(objectMapper.writeValueAsString(scanHelper.getAllUrl()));
    }
}