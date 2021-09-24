package cn.shijh.argmous.factory.rule;

import cn.shijh.argmous.annotation.IsRule;
import cn.shijh.argmous.annotation.ParamCheck;
import cn.shijh.argmous.factory.ValidationRuleFactory;
import cn.shijh.argmous.model.ValidationRule;
import cn.shijh.argmous.util.AnnotationBeanUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

public class DefaultValidationRuleFactory implements ValidationRuleFactory {
    @Override
    public Collection<ValidationRule> createFromAnnotations(ParamCheck[] paramChecks) {
        return Arrays.stream(paramChecks)
                .map(this::createFromAnnotation)
                .collect(Collectors.toList());
    }

    @Override
    public ValidationRule createFromAnnotation(ParamCheck paramCheck) {
        ValidationRule validationRule = new ValidationRule();
        AnnotationBeanUtils.copyProperties(paramCheck, validationRule);
        return validationRule;
    }

    @Override
    public Collection<ValidationRule> createFromBean(Object bean, String name) {
        Field[] fields = bean.getClass().getDeclaredFields();
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