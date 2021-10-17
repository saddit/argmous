package top.pressed.argmous;

import top.pressed.argmous.annotation.Valid;

import java.util.Collection;

public interface TargetService {
    void test1(String s);

    void test2(String a, Integer b);

    void test3(Collection<String> ss);

    void testBean(TestBean bean);

    void testBean2(TestBean bean, String s);

    void testBeanOverride(TestBean bean);

    void test4(@Valid("bean") TestBean bean);

    void testBeanArray(Collection<TestBean> testBeans, TestBean b);
}
