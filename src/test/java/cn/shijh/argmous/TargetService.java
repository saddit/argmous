package cn.shijh.argmous;

import cn.shijh.argmous.annotation.ArrayParamCheck;
import cn.shijh.argmous.annotation.ParamCheck;
import cn.shijh.argmous.annotation.ParamChecks;
import cn.shijh.argmous.annotation.Valid;

import java.util.Collection;

public interface TargetService {
    void test1(String s);

    void test2(String a, Integer b);

    void test3(Collection<String> ss);

    void testBean(TestBean bean);

    void testBeanOverride(TestBean bean);
}
