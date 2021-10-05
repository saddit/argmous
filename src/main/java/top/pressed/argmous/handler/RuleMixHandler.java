package top.pressed.argmous.handler;

import top.pressed.argmous.model.ValidationRule;

import java.util.Collection;

public interface RuleMixHandler {
    Collection<ValidationRule> mix(Collection<ValidationRule> beanRule, Collection<ValidationRule> methodRule);
}
