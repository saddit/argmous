package top.pressed.argmous.manager.impl;

import lombok.AllArgsConstructor;
import top.pressed.argmous.StandardInstanceBean;
import top.pressed.argmous.exception.StandardInitException;
import top.pressed.argmous.manager.ValidatorInject;
import top.pressed.argmous.manager.ValidatorManager;
import top.pressed.argmous.model.ArgumentInfo;
import top.pressed.argmous.model.ValidationRule;
import top.pressed.argmous.validator.RuleValidator;
import top.pressed.argmous.validator.impl.RegexpValidator;
import top.pressed.argmous.validator.impl.RequiredValidator;
import top.pressed.argmous.validator.impl.SizeValidator;
import top.pressed.argmous.validator.impl.ValueRangeValidator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

@AllArgsConstructor
public class DefaultValidatorManager implements ValidatorManager, StandardInstanceBean, ValidatorInject {

    protected final Collection<RuleValidator> validators;

    public DefaultValidatorManager() {
        validators = new ArrayList<>(5);
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

    @Override
    public void addValidators(Collection<RuleValidator> ruleValidators) {
        this.validators.addAll(ruleValidators);
    }

    @Override
    public void afterInitialize() throws StandardInitException {
        if (validators.isEmpty()) {
            validators.addAll(Arrays.asList(
                    new RequiredValidator(),
                    new RegexpValidator(),
                    new SizeValidator(),
                    new ValueRangeValidator()
            ));
        }
    }
}
