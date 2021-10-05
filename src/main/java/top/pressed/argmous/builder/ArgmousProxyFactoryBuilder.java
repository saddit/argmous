package top.pressed.argmous.builder;

import top.pressed.argmous.factory.ArgmousProxyFactory;
import top.pressed.argmous.factory.ArgumentInfoFactory;
import top.pressed.argmous.factory.ValidationRuleFactory;
import top.pressed.argmous.factory.impl.DefaultArgumentInfoFactory;
import top.pressed.argmous.factory.impl.JDKProxyFactory;
import top.pressed.argmous.factory.impl.DefaultValidationRuleFactory;
import top.pressed.argmous.manager.validation.ValidationManager;
import top.pressed.argmous.manager.validation.impl.DefaultValidationManager;
import top.pressed.argmous.manager.validator.ValidatorManager;
import top.pressed.argmous.manager.validator.impl.DefaultValidatorManager;
import top.pressed.argmous.service.impl.ArgmousServiceImpl;
import top.pressed.argmous.validator.RuleValidator;

import java.util.ArrayList;
import java.util.Collection;

public class ArgmousProxyFactoryBuilder {
    private ValidationRuleFactory validationRuleFactory;
    private ArgumentInfoFactory argumentInfoFactory;
    private ValidationManager validationManager;
    private ValidatorManager validatorManager;
    private Collection<RuleValidator> ruleValidators = new ArrayList<>();

    public ArgmousProxyFactoryBuilder setValidationManager(ValidationManager validationManager) {
        this.validationManager = validationManager;
        return this;
    }

    public ArgmousProxyFactoryBuilder setValidatorManager(ValidatorManager validatorManager) {
        this.validatorManager = validatorManager;
        return this;
    }

    public ArgmousProxyFactoryBuilder addValidator(RuleValidator ruleValidator) {
        this.ruleValidators.add(ruleValidator);
        return this;
    }

    public ArgmousProxyFactoryBuilder setValidationRuleFactory(ValidationRuleFactory validationRuleFactory) {
        this.validationRuleFactory = validationRuleFactory;
        return this;
    }

    public ArgmousProxyFactoryBuilder setArgumentInfoFactory(ArgumentInfoFactory argumentInfoFactory) {
        this.argumentInfoFactory = argumentInfoFactory;
        return this;
    }

    public ArgmousProxyFactory build() {
        if (validatorManager == null) {
            validatorManager = new DefaultValidatorManager();
        }
        if (validationManager == null) {
            validationManager = new DefaultValidationManager(this.validatorManager);
            ((DefaultValidatorManager) validatorManager).addValidators(this.ruleValidators);
        }
        if (argumentInfoFactory == null) {
            argumentInfoFactory = new DefaultArgumentInfoFactory();
        }
        if (validationRuleFactory == null) {
            validationRuleFactory = new DefaultValidationRuleFactory();
        }

        ArgmousServiceImpl argmousService = new ArgmousServiceImpl(validationManager);

        return new JDKProxyFactory(validationRuleFactory,argumentInfoFactory,argmousService);
    }

}
