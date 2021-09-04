package cn.shijh.argmous.test;

import cn.shijh.argmous.spring.context.ArrayParamCheck;
import cn.shijh.argmous.spring.context.ParamCheck;
import cn.shijh.argmous.spring.context.ParamChecks;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TestComponent {

    @ParamChecks({
            @ParamCheck(include = {"string"}, size = {-1, 4}, regexp = "a.*"),
            @ParamCheck(include = {"integer"}, range = {"-100","100"}),
            @ParamCheck(include = "aDouble", range = {"","3.14"}),
            @ParamCheck(include = {"list"}, size = 2)
    })
    public void test(TestData data) {
        System.out.println("pass");
    }

    @ParamChecks({
            @ParamCheck(include = {"string"}, size = {-1, 4}, regexp = "a.*"),
            @ParamCheck(include = {"integer"}, range = {"100"}),
            @ParamCheck(include = "aDouble", range = {"","3.14"}),
            @ParamCheck(include = {"list"}, size = 2)
    })
    public void test2(TestData data) {
        System.out.println("pass");
    }

    @ArrayParamCheck(target = "dataList", value = {
            @ParamCheck(include = {"string"}, size = {-1, 4}, regexp = "a.*"),
            @ParamCheck(include = {"integer"}, range = {"-100","100"}),
            @ParamCheck(include = "aDouble", range = {"","3.14"}),
            @ParamCheck(include = {"list"}, size = 2)
    })
    public void arrayTest(List<TestData> dataList) {
        System.out.println("pass");
    }

    @ParamCheck(include = "s", custom = { "customKey=true", "customValue=false" })
    public void testCustom(String s) {
        System.out.println("pass");
    }
}
