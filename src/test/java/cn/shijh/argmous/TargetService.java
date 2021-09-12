package cn.shijh.argmous;

import cn.shijh.argmous.annotation.ArrayParamCheck;
import cn.shijh.argmous.annotation.ParamCheck;
import cn.shijh.argmous.annotation.ParamChecks;
import cn.shijh.argmous.annotation.Valid;

import java.util.Collection;

public interface TargetService {
    @ParamCheck(size = {-1,3}, regexp = "^a+.*")
    void test1(@Valid("s") String s);

    @ParamChecks({
            @ParamCheck(include = "a", size = {2,-1}),
            @ParamCheck(include = "b", range = {"1","3"})
    })
    void test2(@Valid("a") String a, @Valid("b") Integer b);

    @ArrayParamCheck(target = "ss", value = {
            @ParamCheck(size = {2,-1}),
            @ParamCheck(regexp = "^a+.*")
    })
    void test3(@Valid("ss") Collection<String> ss);
}
