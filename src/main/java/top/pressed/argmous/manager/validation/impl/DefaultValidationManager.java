package top.pressed.argmous.manager.validation.impl;

import top.pressed.argmous.StandardInitBean;
import top.pressed.argmous.exception.ParamCheckException;
import top.pressed.argmous.exception.StandardInitException;
import top.pressed.argmous.manager.pool.InstancePoolManager;
import top.pressed.argmous.manager.validator.ValidatorManager;
import top.pressed.argmous.model.ArgumentInfo;
import top.pressed.argmous.model.ValidationRule;

import java.rmi.NoSuchObjectException;
import java.util.Collection;

public class DefaultValidationManager extends AbstractValidationManager implements StandardInitBean {

    protected ValidatorManager validatorManager;

    public DefaultValidationManager() {
    }

    @Override
    protected void doValidate(Collection<ArgumentInfo> currentArgs, ValidationRule currentRule) throws ParamCheckException {
        for (ArgumentInfo arg : currentArgs) {
            String msg = validatorManager.validate(arg, currentRule);
            if (msg != null) {
                throw new ParamCheckException(msg);
            }
        }
    }

    public ValidatorManager getValidatorManager() {
        return this.validatorManager;
    }

    @Override
    public void afterInitialize() throws StandardInitException {
        try {
            if (validatorManager == null) {
                this.validatorManager = InstancePoolManager.instance().getInstance(ValidatorManager.class);
            }
        } catch (NoSuchObjectException e) {
            throw new StandardInitException(e);
        }
    }
}
