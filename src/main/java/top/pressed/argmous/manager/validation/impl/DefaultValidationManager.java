package top.pressed.argmous.manager.validation.impl;

import top.pressed.argmous.exception.ParamCheckException;
import top.pressed.argmous.manager.validator.ValidatorManager;
import top.pressed.argmous.manager.validator.impl.DefaultValidatorManager;
import top.pressed.argmous.model.ArgumentInfo;
import top.pressed.argmous.model.ValidationRule;
import lombok.AllArgsConstructor;

import java.util.Collection;

@AllArgsConstructor
public class DefaultValidationManager extends AbstractValidationManager{

    protected final ValidatorManager validatorManager;

    public DefaultValidationManager() {
        this.validatorManager = new DefaultValidatorManager();
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
}
