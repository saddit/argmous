package top.pressed.argmous.manager;

import top.pressed.argmous.validator.RuleValidator;

import java.util.Collection;

public interface ValidatorInject {
    void addValidators(Collection<RuleValidator> validators);
}
