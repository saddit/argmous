package top.pressed.argmous.service.impl;

import lombok.AllArgsConstructor;
import top.pressed.argmous.StandardInitBean;
import top.pressed.argmous.exception.ParamCheckException;
import top.pressed.argmous.exception.StandardInitException;
import top.pressed.argmous.manager.pool.InstancePoolManager;
import top.pressed.argmous.manager.validation.ValidationManager;
import top.pressed.argmous.model.ArgumentInfo;
import top.pressed.argmous.model.ValidationRule;
import top.pressed.argmous.service.ArgmousService;

import java.rmi.NoSuchObjectException;
import java.util.Collection;

@AllArgsConstructor
public class ArgmousServiceImpl implements ArgmousService, StandardInitBean {
    protected ValidationManager validationManager;

    public ArgmousServiceImpl() {
    }

    @Override
    public void paramCheck(Collection<ArgumentInfo> argumentInfos, Collection<ValidationRule> rules) throws ParamCheckException {
        for (ValidationRule rule : rules) {
            validationManager.validate(argumentInfos, rule);
        }
    }

    @Override
    public void afterInitialize() throws StandardInitException {
        try {
            if (this.validationManager == null) {
                this.validationManager = InstancePoolManager.i().getInstance(ValidationManager.class);
            }
        } catch (NoSuchObjectException e) {
            throw new StandardInitException(e);
        }
    }
}
