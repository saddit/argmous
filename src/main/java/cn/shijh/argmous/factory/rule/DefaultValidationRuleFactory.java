package cn.shijh.argmous.factory.rule;

import cn.shijh.argmous.annotation.ParamCheck;
import cn.shijh.argmous.factory.ValidationRuleFactory;
import cn.shijh.argmous.model.ValidationRule;
import cn.shijh.argmous.util.AnnotationBeanUtils;

import java.util.Arrays;
import java.util.Collection;
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
}
