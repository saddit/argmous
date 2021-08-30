package cn.shijh.argmous.manager.impl;

import cn.shijh.argmous.context.ParamCheck;
import cn.shijh.argmous.exception.ParamCheckException;
import cn.shijh.argmous.manager.ArrayValidationManager;
import cn.shijh.argmous.manager.ValidationManager;
import cn.shijh.argmous.model.ArgumentInfo;
import cn.shijh.argmous.model.ValidationRule;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class AbstractValidationManager implements ValidationManager, ArrayValidationManager {

    protected boolean isTarget(ValidationRule rule, String argName) {
        return rule.getTarget().isEmpty() || rule.getTarget().equals(argName);
    }

    protected boolean isAllowCheck(ValidationRule rule, String paramName) {
        if (!rule.getInclude().isEmpty()) {
            return rule.getInclude().contains(paramName);
        } else if (!rule.getExclude().isEmpty()) {
            return !rule.getExclude().contains(paramName);
        }
        return true;
    }

    protected <T extends ArgumentInfo> Stream<T> filterAllowed(Stream<T> stream, ValidationRule rule) {
        return stream.filter(s -> isTarget(rule, s.getParentName()) && isAllowCheck(rule, s.getName()));
    }

    protected abstract void doValidate(Collection<ArgumentInfo> currentArgs, ValidationRule currentRule) throws ParamCheckException;

    @Override
    public void validate(Collection<ArgumentInfo> argument, ValidationRule rule) throws ParamCheckException {
        List<ArgumentInfo> currentArgs = filterAllowed(argument.stream(), rule)
                .collect(Collectors.toList());
        doValidate(currentArgs, rule);
    }

    @Override
    public void validate(Collection<ArgumentInfo> argument, Collection<ValidationRule> rule, String target) throws ParamCheckException {
        List<ArgumentInfo> elementArg = argument.stream()
                .filter(arg -> Collection.class.isAssignableFrom(arg.getType()))
                .filter(arg -> arg.getName().equals(target))
                .flatMap(argumentInfo -> {
                    Collection<?> coll = (Collection<?>) argumentInfo.getValue();
                    return coll.stream();
                })
                .map(element -> {
                    ArgumentInfo arg = new ArgumentInfo();
                    arg.setType(element.getClass());
                    arg.setValue(element);
                    arg.setParentName(target);
                    return arg;
                }).collect(Collectors.toList());
        rule.forEach(r -> validate(elementArg, r));
    }
}
