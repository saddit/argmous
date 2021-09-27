package cn.shijh.argmous.validator.impl;



import cn.shijh.argmous.model.ValidationRule;
import cn.shijh.argmous.validator.RuleValidator;
import org.apache.commons.lang3.StringUtils;


public class RequiredValidator implements RuleValidator {
    @Override
    public boolean support(Class<?> paramType, ValidationRule rule) {
        return true;
    }

    @Override
    public String errorMessage(ValidationRule rule) {
        return "required";
    }

    @Override
    public boolean validate(Object object, ValidationRule rule) {
        return !rule.getRequired() || (object != null && StringUtils.isNotBlank(object.toString()));
    }
}
