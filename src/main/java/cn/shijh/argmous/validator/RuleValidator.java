package cn.shijh.argmous.validator;


import cn.shijh.argmous.context.ParamCheck;

public interface RuleValidator {
    boolean validate(Object object, ParamCheck rule) throws IllegalStateException;

    String errorMessage(ParamCheck rule);

    boolean support(Class<?> paramType, ParamCheck rule);
}
