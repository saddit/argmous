package top.pressed.argmous.service.impl;

import top.pressed.argmous.exception.ParamCheckException;
import top.pressed.argmous.manager.validation.ValidationManager;
import top.pressed.argmous.manager.validation.impl.DefaultValidationManager;
import top.pressed.argmous.model.ArgumentInfo;
import top.pressed.argmous.model.ValidationRule;
import top.pressed.argmous.service.ArgmousService;
import lombok.AllArgsConstructor;

import java.util.Collection;

@AllArgsConstructor
public class ArgmousServiceImpl implements ArgmousService {
    protected ValidationManager validationManager;

    public ArgmousServiceImpl() {
        this.validationManager = new DefaultValidationManager();
    }

    @Override
    public void paramCheck(Collection<ArgumentInfo> argumentInfos, Collection<ValidationRule> rules) throws ParamCheckException {
        for (ValidationRule rule : rules) {
            validationManager.validate(argumentInfos, rule);
        }
    }
}
