package top.pressed.argmous.factory.impl;

import com.esotericsoftware.reflectasm.FieldAccess;
import top.pressed.argmous.annotation.ArrayParamCheck;
import top.pressed.argmous.annotation.IsRule;
import top.pressed.argmous.annotation.ParamCheck;
import top.pressed.argmous.exception.RuleCreateException;
import top.pressed.argmous.factory.ValidationRuleFactory;
import top.pressed.argmous.model.ValidationRule;
import top.pressed.argmous.util.AnnotationBeanUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

public class DefaultValidationRuleFactory implements ValidationRuleFactory {
    @Override
    public Collection<ValidationRule> createFromAnnotations(ParamCheck[] paramChecks, String defaultTargetName) {
        return Arrays.stream(paramChecks)
                .map(i->this.createFromAnnotation(i, defaultTargetName))
                .collect(Collectors.toList());
    }
    
    @Override
    public ValidationRule createFromAnnotation(ParamCheck paramCheck, String defaultTargetName) {
        ValidationRule validationRule = new ValidationRule();
        AnnotationBeanUtils.copyProperties(paramCheck, validationRule);
        if (validationRule.getTarget().isEmpty()) {
            validationRule.setTarget(defaultTargetName);
        }
        return validationRule;
    }

    @Override
    public Collection<ValidationRule> createFromAnnotation(ArrayParamCheck arrayParamCheck, String defaultTargetName) throws RuleCreateException {
        String targetName = arrayParamCheck.target().isEmpty() ? defaultTargetName : arrayParamCheck.target();
        Collection<ValidationRule> fromAnnotations = createFromAnnotations(arrayParamCheck.value(), targetName);
        ValidationRule selfRule = createFromAnnotation(arrayParamCheck.self(), targetName);
        selfRule.addInclude(targetName);
        fromAnnotations.add(selfRule);
        return fromAnnotations;
    }

    @Override
    public Collection<ValidationRule> createFromBean(Class<?> type, String name) {
        Field[] fields = type.getDeclaredFields();
        Collection<ValidationRule> rules = new ArrayList<>(fields.length);
        AtomicBoolean shouldValid = new AtomicBoolean(false);
        for (Field field : fields) {
            ValidationRule rule = ValidationRule.empty();
            rule.setTarget(name);
            rule.addInclude(field.getName());
            shouldValid.set(false);
            for (Annotation annotation : field.getDeclaredAnnotations()) {
                IsRule isRule = annotation.annotationType().getDeclaredAnnotation(IsRule.class);
                Optional.ofNullable(isRule).ifPresent(ir -> {
                    shouldValid.set(true);
                    AnnotationBeanUtils.copyProperties(annotation, rule,
                            Collections.singletonMap("value", ir.value()));
                });
            }
            if (shouldValid.get()) {
                rules.add(rule);
            }
        }
        return rules;
    }
}
