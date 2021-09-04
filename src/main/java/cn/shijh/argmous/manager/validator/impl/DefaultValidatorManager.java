package cn.shijh.argmous.manager.validator.impl;

import cn.shijh.argmous.manager.validator.ValidatorManager;
import cn.shijh.argmous.model.ArgumentInfo;
import cn.shijh.argmous.model.ValidationRule;
import cn.shijh.argmous.validator.RuleValidator;
import lombok.AllArgsConstructor;

import java.util.Collection;

@AllArgsConstructor
public class DefaultValidatorManager implements ValidatorManager {

    protected Collection<RuleValidator> validators;

    @Override
    public String validate(ArgumentInfo argument, ValidationRule rule) {
        for (RuleValidator validator : validators) {
            if (validator.support(argument.getType(), rule) && !validator.validate(argument.getValue(), rule)) {
                return validator.errorMessage(rule);
            }
        }
        return null;
    }
}
