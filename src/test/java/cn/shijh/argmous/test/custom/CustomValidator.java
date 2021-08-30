package cn.shijh.argmous.test.custom;

import cn.shijh.argmous.context.ParamCheck;
import cn.shijh.argmous.util.CustomizeUtils;
import cn.shijh.argmous.validator.RuleValidator;
import org.springframework.stereotype.Component;

@Component
public class CustomValidator implements RuleValidator {
    private final String CUSTOM_KEY = "customKey";
    @Override
    public boolean validate(Object object, ParamCheck rule) throws IllegalStateException {
        String value = CustomizeUtils.getValue(rule, CUSTOM_KEY);
        if (value != null) {
            System.out.println("value is " + value);
            return object.toString().equals(value);
        }
        throw new IllegalArgumentException("not support");
    }

    @Override
    public String errorMessage(ParamCheck rule) {
        return "custom "+ CUSTOM_KEY +" error";
    }

    @Override
    public boolean support(Class<?> paramType, ParamCheck rule) {
        return CustomizeUtils.hasKeyValue(rule, CUSTOM_KEY);
    }
}
