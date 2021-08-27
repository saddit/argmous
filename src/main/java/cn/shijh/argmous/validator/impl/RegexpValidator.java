package cn.shijh.argmous.validator.impl;



import cn.shijh.argmous.context.ParamCheck;
import cn.shijh.argmous.validator.RuleValidator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class RegexpValidator implements RuleValidator {
    @Override
    public boolean support(Class<?> paramType, ParamCheck rule) {
        return String.class.isAssignableFrom(paramType) && !rule.regexp().isEmpty();
    }

    @Override
    public String errorMessage(ParamCheck rule) {
        return "format error";
    }

    @Override
    public boolean validate(Object object, ParamCheck rule) {
        String regexp = rule.regexp();
        if (StringUtils.isNotBlank(regexp) && object instanceof String) {
             return object.toString().matches(regexp);
        }
        return true;
    }
}
