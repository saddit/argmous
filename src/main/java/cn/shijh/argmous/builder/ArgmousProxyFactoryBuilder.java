package cn.shijh.argmous.builder;

import cn.shijh.argmous.factory.ArgmousProxyFactory;
import cn.shijh.argmous.factory.ArgumentInfoFactory;
import cn.shijh.argmous.factory.ValidationRuleFactory;
import cn.shijh.argmous.factory.arg.DefaultArgumentInfoFactory;
import cn.shijh.argmous.factory.proxy.JDKProxyFactory;
import cn.shijh.argmous.factory.rule.DefaultValidationRuleFactory;
import cn.shijh.argmous.manager.validation.ArrayValidationManager;
import cn.shijh.argmous.manager.validation.ValidationManager;
import cn.shijh.argmous.manager.validation.impl.DefaultValidationManager;
import cn.shijh.argmous.manager.validator.ValidatorManager;
import cn.shijh.argmous.manager.validator.impl.DefaultValidatorManager;
import cn.shijh.argmous.service.impl.ArgmousServiceImpl;
import cn.shijh.argmous.validator.RuleValidator;

import java.util.ArrayList;
import java.util.Collection;

public class ArgmousProxyFactoryBuilder {
    private ValidationRuleFactory validationRuleFactory;
    private ArgumentInfoFactory argumentInfoFactory;
    private ValidationManager validationManager;
    private ArrayValidationManager arrayValidationManager;
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

    public ArgmousProxyFactoryBuilder setArrayValidatorManager(ArrayValidationManager arrayValidatorManager) {
        this.arrayValidationManager = arrayValidatorManager;
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
        if (arrayValidationManager == null) {
            arrayValidationManager = validatorManager instanceof ArrayValidationManager?
                    (ArrayValidationManager)validationManager : new DefaultValidationManager(this.validatorManager);
        }
        if (argumentInfoFactory == null) {
            argumentInfoFactory = new DefaultArgumentInfoFactory();
        }
        if (validationRuleFactory == null) {
            validationRuleFactory = new DefaultValidationRuleFactory();
        }

        ArgmousServiceImpl argmousService = new ArgmousServiceImpl(validationManager, arrayValidationManager);

        return new JDKProxyFactory(validationRuleFactory,argumentInfoFactory,argmousService);
    }

}
