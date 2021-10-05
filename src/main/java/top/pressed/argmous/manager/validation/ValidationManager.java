package top.pressed.argmous.manager.validation;

import top.pressed.argmous.exception.ParamCheckException;
import top.pressed.argmous.model.ArgumentInfo;
import top.pressed.argmous.model.ValidationRule;

import java.util.Collection;

public interface ValidationManager {
    void validate(Collection<ArgumentInfo> argument, ValidationRule rule) throws ParamCheckException;
}
