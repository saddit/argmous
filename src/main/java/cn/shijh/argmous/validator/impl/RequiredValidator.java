package cn.shijh.argmous.validator.impl;



import cn.shijh.argmous.context.ParamCheck;
import cn.shijh.argmous.validator.RuleValidator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class RequiredValidator implements RuleValidator {
    @Override
    public boolean support(Class<?> paramType, ParamCheck rule) {
        return true;
    }

    @Override
    public String errorMessage(ParamCheck rule) {
        return "required";
    }

    @Override
    public boolean validate(Object object, ParamCheck rule) {
        return (object != null && StringUtils.isNotBlank(object.toString())) || !rule.required();
    }
}
