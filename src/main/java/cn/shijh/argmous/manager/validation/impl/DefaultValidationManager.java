package cn.shijh.argmous.manager.validation.impl;

import cn.shijh.argmous.exception.ParamCheckException;
import cn.shijh.argmous.manager.validator.ValidatorManager;
import cn.shijh.argmous.model.ArgumentInfo;
import cn.shijh.argmous.model.ValidationRule;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;

@AllArgsConstructor
public class DefaultValidationManager extends AbstractValidationManager{

    protected ValidatorManager validatorManager;

    @Override
    protected void doValidate(Collection<ArgumentInfo> currentArgs, ValidationRule currentRule) throws ParamCheckException {
        for (ArgumentInfo arg : currentArgs) {
            String msg = validatorManager.validate(arg, currentRule);
            if (msg != null) {
                throw new ParamCheckException(msg);
            }
        }
    }
}
