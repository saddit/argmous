package cn.shijh.argmous.manager;

import cn.shijh.argmous.exception.ParamCheckException;
import cn.shijh.argmous.model.ArgumentInfo;
import cn.shijh.argmous.model.ValidationRule;

import java.util.Collection;

public interface ArrayValidationManager {
    void validate(Collection<ArgumentInfo> argument, Collection<ValidationRule> rule, String target) throws ParamCheckException;
}
