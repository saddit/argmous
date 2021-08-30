package cn.shijh.argmous.manager;

import cn.shijh.argmous.exception.ParamCheckException;
import cn.shijh.argmous.model.ArgumentInfo;
import cn.shijh.argmous.model.ValidationRule;

import java.util.Collection;

public interface ValidationManager {
    void validate(Collection<ArgumentInfo> argument, ValidationRule rule) throws ParamCheckException;
}
