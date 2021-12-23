package top.pressed.argmous.handler;

import top.pressed.argmous.annotation.ArrayParamCheck;
import top.pressed.argmous.annotation.IsRule;
import top.pressed.argmous.annotation.ParamCheck;
import top.pressed.argmous.annotation.ParamChecks;
import top.pressed.argmous.factory.impl.MethodValidationRuleFactory;
import top.pressed.argmous.model.ValidationRule;
import top.pressed.argmous.util.AnnotationBeanUtils;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

public class RuleAnnotationProcessor {
    private ValidationRule processParamCheck(ParamCheck paramCheck) {
        ValidationRule validationRule = new ValidationRule();
        AnnotationBeanUtils.copyProperties(paramCheck, validationRule);
        return validationRule;
    }

    private Collection<ValidationRule> processArrayParamCheck(ArrayParamCheck arrayParamCheck) {
        Collection<ValidationRule> fromAnnotations = Arrays.stream(arrayParamCheck.value())
                .map(this::processParamCheck)
                .collect(Collectors.toList());
        ValidationRule selfRule = processParamCheck(arrayParamCheck.self());
        selfRule.getCustom().add(MethodValidationRuleFactory.ARRAY_SELF_RULE);
        fromAnnotations.add(selfRule);
        return fromAnnotations;
    }

    private Collection<ValidationRule> processParamChecks(ParamChecks paramChecks) {
        return Arrays.stream(paramChecks.value())
                .map(this::processParamCheck)
                .collect(Collectors.toList());
    }

    public Collection<ValidationRule> process(Annotation annotation) {
        if (annotation instanceof ParamCheck) {
            return Collections.singletonList(processParamCheck((ParamCheck) annotation));
        } else if (annotation instanceof ArrayParamCheck) {
            return processArrayParamCheck((ArrayParamCheck) annotation);
        } else if (annotation instanceof ParamChecks) {
            return processParamChecks((ParamChecks) annotation);
        }
        throw new IllegalArgumentException("unsupported annotation " + annotation.getClass().getName());
    }

    public boolean processBeanAnnotation(Annotation annotation, ValidationRule rule) {
        IsRule ir = annotation.annotationType().getDeclaredAnnotation(IsRule.class);
        if (ir != null) {
            if (rule == null) {
                rule = ValidationRule.empty();
            }
            AnnotationBeanUtils.copyProperties(annotation, rule,
                    Collections.singletonMap("value", ir.value()));
            return true;
        }
        return false;
    }
}
