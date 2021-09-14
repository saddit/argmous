package cn.shijh.argmous.manager.validator.impl;

import cn.shijh.argmous.manager.validator.ValidatorManager;
import cn.shijh.argmous.model.ArgumentInfo;
import cn.shijh.argmous.model.ValidationRule;
import cn.shijh.argmous.validator.RuleValidator;
import cn.shijh.argmous.validator.impl.RegexpValidator;
import cn.shijh.argmous.validator.impl.RequiredValidator;
import cn.shijh.argmous.validator.impl.SizeValidator;
import cn.shijh.argmous.validator.impl.ValueRangeValidator;
import lombok.AllArgsConstructor;

import java.util.Arrays;
import java.util.Collection;

@AllArgsConstructor
public class DefaultValidatorManager implements ValidatorManager {

    protected final Collection<RuleValidator> validators;

    public DefaultValidatorManager() {
        validators = Arrays.asList(
                new RequiredValidator(),
                new RegexpValidator(),
                new SizeValidator(),
                new ValueRangeValidator()
        );
    }

    @Override
    public String validate(ArgumentInfo argument, ValidationRule rule) {
        for (RuleValidator validator : validators) {
            if (validator.support(argument.getType(), rule) && !validator.validate(argument.getValue(), rule)) {
                return argument.getName() + "," + validator.errorMessage(rule);
            }
        }
        return null;
    }

    public void addValidators(Collection<RuleValidator> ruleValidators) {
        this.validators.addAll(ruleValidators);
    }
}
