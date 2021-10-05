package top.pressed.argmous.service;

import top.pressed.argmous.exception.ParamCheckException;
import top.pressed.argmous.model.ArgumentInfo;
import top.pressed.argmous.model.ValidationRule;

import java.util.Collection;

public interface ArgmousService {
    void paramCheck(Collection<ArgumentInfo> argumentInfos, Collection<ValidationRule> rules) throws ParamCheckException;
}
