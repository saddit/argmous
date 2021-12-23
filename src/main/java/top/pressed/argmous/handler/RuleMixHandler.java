package top.pressed.argmous.handler;

import top.pressed.argmous.factory.ValidationRuleFactory;
import top.pressed.argmous.model.ValidationRule;

import java.util.Collection;
import java.util.Map;

public interface RuleMixHandler {
    Collection<ValidationRule> handle(Map<Class<? extends ValidationRuleFactory>, Collection<ValidationRule>> dataMap);
}
