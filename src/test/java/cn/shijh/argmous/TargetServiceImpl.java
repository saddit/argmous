package cn.shijh.argmous;

import cn.shijh.argmous.annotation.ArrayParamCheck;
import cn.shijh.argmous.annotation.ParamCheck;
import cn.shijh.argmous.annotation.ParamChecks;
import cn.shijh.argmous.annotation.Valid;

import java.util.Collection;

@SuppressWarnings("all")
public class TargetServiceImpl implements TargetService {

    @Override
    @ParamCheck(size = {-1,3}, regexp = "^a+.*", target = "s")
    public void test1(@Valid("s") String  s) {
        System.out.println("pass");
    }

    @ParamCheck(size = {-1, 2}, target = "s")
    @Override
    public void testBean2(TestBean bean, @Valid("s") String s) {
        System.out.println("success");
    }

    @Override
    @ParamChecks({
            @ParamCheck(size = {2,-1}, target = "a"),
            @ParamCheck(range = {"1","3"}, target = "b")
    })
    public void test2(@Valid("a") String a, @Valid("b") Integer b) {
        System.out.println("pass");
    }

    @ArrayParamCheck(target = "ss", value = {
            @ParamCheck(size = {2,-1}),
            @ParamCheck(regexp = "^a+.*")
    }, self = @ParamCheck(required = true, size = {1, -1}, target = "ss"))
    public void test3(@Valid("ss") Collection<String> ss) {
        System.out.println("pass");
    }

    @Override
    public void testBean(@Valid("bean") TestBean bean) {
        System.out.println(bean);
    }

    @ParamCheck(include = "name", regexp = "b.*", target = "bean")
    @Override
    public void testBeanOverride(@Valid("bean") TestBean bean) {
        System.out.println(bean);
    }

    @ParamCheck(include = "num", range = {"0", "11"}, target = "bean")
    @Override
    public void test4(@Valid("bean") TestBean bean) {
        System.out.println(bean);
    }

}
