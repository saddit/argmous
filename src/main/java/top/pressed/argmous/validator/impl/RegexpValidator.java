package top.pressed.argmous.validator.impl;


import top.pressed.argmous.model.ValidationRule;
import top.pressed.argmous.validator.RuleValidator;
import org.apache.commons.lang3.StringUtils;

public class RegexpValidator implements RuleValidator {
    @Override
    public boolean support(Class<?> paramType, ValidationRule rule) {
        return String.class.isAssignableFrom(paramType) && !rule.getRegexp().isEmpty();
    }

    @Override
    public String errorMessage(ValidationRule rule) {
        return "format error";
    }

    @Override
    public boolean validate(Object object, ValidationRule rule) {
        String regexp = rule.getRegexp();
        if (StringUtils.isNotBlank(regexp) && object instanceof String) {
            return object.toString().matches(regexp);
        }
        return true;
    }
}
