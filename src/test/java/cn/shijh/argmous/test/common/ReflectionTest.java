package cn.shijh.argmous.test.common;

import cn.shijh.argmous.model.ValidationRule;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.Arrays;

public class ReflectionTest {

    @Test
    void reflect1() throws Exception {
        ValidationRule rule = new ValidationRule();
        Field required = rule.getClass().getDeclaredField("required");
        Object val = true;
        required.setAccessible(true);
        required.set(rule, val);
        System.out.println(rule);
    }

    @Test
    void reflect2() throws Exception {
        ValidationRule rule = new ValidationRule();
        Field include = rule.getClass().getDeclaredField("include");
        Object[] s = new String[] {"1","2","3"};
        include.setAccessible(true);
        include.set(rule, Arrays.asList(s));
        //include.set(rule, s);     wrong
        System.out.println(rule);
    }

}
