package cn.shijh.argmous.test.custom;

import cn.shijh.argmous.exception.ParamCheckException;
import cn.shijh.argmous.test.TestApplication;
import cn.shijh.argmous.test.TestComponent;
import cn.shijh.argmous.util.CustomUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = TestApplication.class)
public class CustomUtilsUnitTest {
    @Autowired
    private TestComponent testComponent;

    @Test
    void customUtils() throws Exception {
        testComponent.testCustom("true");
    }

    @Test
    void test2() throws Exception {
        try {
            testComponent.testCustom("false");
        } catch (ParamCheckException e) {
            System.out.println(e.getMessage());
            return;
        }
        throw new IllegalStateException("test fail");
    }
}
