package cn.shijh.argmous.handler;

import cn.shijh.argmous.annotation.ArrayParamCheck;
import cn.shijh.argmous.annotation.ParamCheck;
import cn.shijh.argmous.annotation.ParamChecks;
import cn.shijh.argmous.exception.ParamCheckException;
import cn.shijh.argmous.factory.ArgumentInfoFactory;
import cn.shijh.argmous.factory.ValidationRuleFactory;
import cn.shijh.argmous.factory.arg.DefaultArgumentInfoFactory;
import cn.shijh.argmous.factory.rule.DefaultValidationRuleFactory;
import cn.shijh.argmous.model.ArgumentInfo;
import cn.shijh.argmous.model.ValidationRule;
import cn.shijh.argmous.service.ArgmousService;
import cn.shijh.argmous.service.impl.ArgmousServiceImpl;
import lombok.AllArgsConstructor;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.LinkedList;

@AllArgsConstructor
public class ParamCheckInvocationHandler implements InvocationHandler {
    private final ValidationRuleFactory validationRuleFactory;
    private final ArgumentInfoFactory argumentInfoFactory;
    private final ArgmousService argmousService;
    private final Object target;

    public ParamCheckInvocationHandler(Object target) {
        this.validationRuleFactory = new DefaultValidationRuleFactory();
        this.argumentInfoFactory = new DefaultArgumentInfoFactory();
        this.argmousService = new ArgmousServiceImpl();
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws ParamCheckException, InvocationTargetException, IllegalAccessException {
        Collection<ArgumentInfo> argumentInfos = argumentInfoFactory.createFromMethod(method, args);
        Collection<ValidationRule> validationRules = new LinkedList<>();
        ParamCheck single = method.getAnnotation(ParamCheck.class);
        ParamChecks multi = method.getAnnotation(ParamChecks.class);
        ArrayParamCheck array = method.getAnnotation(ArrayParamCheck.class);
        if (single != null) {
            validationRules.add(validationRuleFactory.createFromAnnotation(single));
            argmousService.paramCheck(argumentInfos, validationRules);
        } else if (multi != null) {
            validationRules.addAll(validationRuleFactory.createFromAnnotations(multi.value()));
            argmousService.paramCheck(argumentInfos, validationRules);
        } else if (array != null) {
            validationRules.addAll(validationRuleFactory.createFromAnnotations(array.value()));
            argmousService.arrayParamCheck(argumentInfos, validationRules, array.target());
        }
        return method.invoke(target, args);
    }
}
