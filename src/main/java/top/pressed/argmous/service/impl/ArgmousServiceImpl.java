package top.pressed.argmous.service.impl;

import lombok.AllArgsConstructor;
import top.pressed.argmous.StandardInstanceBean;
import top.pressed.argmous.annotation.ArrayParamCheck;
import top.pressed.argmous.exception.ParamCheckException;
import top.pressed.argmous.exception.StandardInitException;
import top.pressed.argmous.factory.ArgumentInfoFactory;
import top.pressed.argmous.factory.ValidationRuleFactory;
import top.pressed.argmous.manager.GetInstance;
import top.pressed.argmous.manager.ValidationManager;
import top.pressed.argmous.model.ArgumentInfo;
import top.pressed.argmous.model.ValidationRule;
import top.pressed.argmous.service.ArgmousService;

import java.lang.reflect.Method;
import java.rmi.NoSuchObjectException;
import java.util.Collection;

@AllArgsConstructor
public class ArgmousServiceImpl implements ArgmousService, StandardInstanceBean {
    protected ValidationManager validationManager;
    private ValidationRuleFactory ruleFactory;
    private ArgumentInfoFactory argumentFactory;

    public ArgmousServiceImpl() {
    }

    @Override
    public void startValidate(Method method, Object[] values, String[] names) throws ParamCheckException {
        boolean ignoreArray = method.getAnnotation(ArrayParamCheck.class) == null;
        Collection<ValidationRule> rules = ruleFactory.create(method, values, names, ignoreArray);
        if (rules.isEmpty()) {
            return;
        }
        Collection<ArgumentInfo> args = argumentFactory.create(method, values, names, ignoreArray);
        if (args.isEmpty()) {
            return;
        }
        for (ValidationRule rule : rules) {
            validationManager.validate(args, rule);
        }
    }

    @Override
    public void afterInitialize(GetInstance getter) throws StandardInitException {
        try {
            if (this.validationManager == null) {
                this.validationManager = getter.getInstance(ValidationManager.class);
            }
            if (this.argumentFactory == null) {
                this.argumentFactory = getter.getInstance(ArgumentInfoFactory.class);
            }
            if (this.ruleFactory == null) {
                this.ruleFactory = getter.getInstance(ValidationRuleFactory.class);
            }
        } catch (NoSuchObjectException e) {
            throw new StandardInitException(e);
        }
    }
}
