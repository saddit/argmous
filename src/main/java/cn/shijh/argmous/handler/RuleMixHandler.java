package cn.shijh.argmous.handler;

import cn.shijh.argmous.model.ValidationRule;

import java.util.Collection;

public interface RuleMixHandler {
    Collection<ValidationRule> mix(Collection<ValidationRule> beanRule, Collection<ValidationRule> methodRule);
}
