package top.pressed.argmous.manager.validation.impl;

import top.pressed.argmous.exception.ParamCheckException;
import top.pressed.argmous.manager.validation.ValidationManager;
import top.pressed.argmous.model.ArgumentInfo;
import top.pressed.argmous.model.ValidationRule;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class AbstractValidationManager implements ValidationManager {

    protected boolean isTarget(ValidationRule rule, String argName) {
        return rule.getTarget().isEmpty() || rule.getTarget().equals(argName);
    }

    protected boolean isAllowCheck(ValidationRule rule, String paramName) {
        if (!rule.getInclude().isEmpty()) {
            return rule.getInclude().contains(paramName);
        }
        return true;
    }

    protected <T extends ArgumentInfo> Stream<T> filterAllowed(Stream<T> stream, ValidationRule rule) {
        return stream.filter(s -> isTarget(rule, s.getBelongTo()) && isAllowCheck(rule, s.getName()));
    }

    protected abstract void doValidate(Collection<ArgumentInfo> currentArgs, ValidationRule currentRule) throws ParamCheckException;

    @Override
    public void validate(Collection<ArgumentInfo> argument, ValidationRule rule) throws ParamCheckException {
        List<ArgumentInfo> currentArgs = filterAllowed(argument.stream(), rule)
                .collect(Collectors.toList());
        doValidate(currentArgs, rule);
    }
}
