package cn.shijh.argmous.service;

import cn.shijh.argmous.exception.ParamCheckException;
import cn.shijh.argmous.model.ArgumentInfo;
import cn.shijh.argmous.model.ValidationRule;
import cn.shijh.argmous.validator.RuleValidator;

import java.util.Collection;

public interface ArgmousService {
    void paramCheck(Collection<ArgumentInfo> argumentInfos, Collection<ValidationRule> rules) throws ParamCheckException;
}
