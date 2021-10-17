package top.pressed.argmous.manager.validator.impl;

import lombok.AllArgsConstructor;
import top.pressed.argmous.manager.validator.ValidatorManager;
import top.pressed.argmous.model.ArgumentInfo;
import top.pressed.argmous.model.ValidationRule;
import top.pressed.argmous.validator.RuleValidator;
import top.pressed.argmous.validator.impl.RegexpValidator;
import top.pressed.argmous.validator.impl.RequiredValidator;
import top.pressed.argmous.validator.impl.SizeValidator;
import top.pressed.argmous.validator.impl.ValueRangeValidator;

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
                return argument.getBelongTo() + "." + argument.getName() + ":" + validator.errorMessage(rule);
            }
        }
        return null;
    }

    public void addValidators(Collection<RuleValidator> ruleValidators) {
        this.validators.addAll(ruleValidators);
    }
}
