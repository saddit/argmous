package cn.shijh.argmous.service.impl;

import cn.shijh.argmous.exception.ParamCheckException;
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
