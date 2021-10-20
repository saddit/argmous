package top.pressed.argmous.manager.validator;

import top.pressed.argmous.validator.RuleValidator;

import java.util.Collection;

public interface ValidatorInject {
    void addValidators(Collection<RuleValidator> validators);
}
