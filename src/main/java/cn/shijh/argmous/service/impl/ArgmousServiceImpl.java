package cn.shijh.argmous.service.impl;

import cn.shijh.argmous.exception.ParamCheckException;
import cn.shijh.argmous.manager.validation.ArrayValidationManager;
import cn.shijh.argmous.manager.validation.ValidationManager;
import cn.shijh.argmous.manager.validation.impl.DefaultValidationManager;
import cn.shijh.argmous.model.ArgumentInfo;
import cn.shijh.argmous.model.ValidationRule;
import cn.shijh.argmous.service.ArgmousService;
import lombok.AllArgsConstructor;

import java.util.Collection;

@AllArgsConstructor
public class ArgmousServiceImpl implements ArgmousService {
    protected ValidationManager validationManager;
    protected ArrayValidationManager arrayValidationManager;

    public ArgmousServiceImpl() {
        DefaultValidationManager defaultValidationManager = new DefaultValidationManager();
        this.validationManager = defaultValidationManager;
        this.arrayValidationManager = defaultValidationManager;
    }

    @Override
    public void paramCheck(Collection<ArgumentInfo> argumentInfos, Collection<ValidationRule> rules) throws ParamCheckException {
        for (ValidationRule rule : rules) {
            validationManager.validate(argumentInfos, rule);
        }
    }

    @Override
    public void arrayParamCheck(Collection<ArgumentInfo> argumentInfos, Collection<ValidationRule> rules, String targetName) throws ParamCheckException {
        arrayValidationManager.validate(argumentInfos, rules, targetName);
    }
}
