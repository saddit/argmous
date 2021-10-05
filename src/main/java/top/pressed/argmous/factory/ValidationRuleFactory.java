package top.pressed.argmous.factory;

import top.pressed.argmous.annotation.ArrayParamCheck;
import top.pressed.argmous.annotation.ParamCheck;
import top.pressed.argmous.exception.RuleCreateException;
import top.pressed.argmous.model.ValidationRule;

import java.util.Collection;

public interface ValidationRuleFactory {
    Collection<ValidationRule> createFromAnnotations(ParamCheck[] paramChecks, String defaultTargetName) throws RuleCreateException;

    Collection<ValidationRule> createFromAnnotation(ArrayParamCheck arrayParamCheck, String defaultTargetName) throws RuleCreateException;

    ValidationRule createFromAnnotation(ParamCheck paramCheck, String defaultTargetName) throws RuleCreateException;

    Collection<ValidationRule> createFromBean(Class<?> beanType, String name) throws RuleCreateException;
}
