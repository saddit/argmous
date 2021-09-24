package cn.shijh.argmous.handler.impl;

import cn.shijh.argmous.annotation.ArrayParamCheck;
import cn.shijh.argmous.annotation.ParamCheck;
import cn.shijh.argmous.annotation.ParamChecks;
import cn.shijh.argmous.exception.ParamCheckException;
import cn.shijh.argmous.factory.ArgumentInfoFactory;
import cn.shijh.argmous.factory.ValidationRuleFactory;
import cn.shijh.argmous.factory.arg.DefaultArgumentInfoFactory;
import cn.shijh.argmous.factory.rule.DefaultValidationRuleFactory;
import cn.shijh.argmous.handler.RuleMixHandler;
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
import java.util.List;
import java.util.stream.Collectors;

public class ParamCheckInvocationHandler implements InvocationHandler {
    private final ValidationRuleFactory validationRuleFactory;
    private final ArgumentInfoFactory argumentInfoFactory;
    private final ArgmousService argmousService;
    private RuleMixHandler ruleMixHandler = new MethodToBeanRuleMixHandler();
    private final Object target;

    public ParamCheckInvocationHandler(Object target) {
        this.validationRuleFactory = new DefaultValidationRuleFactory();
        this.argumentInfoFactory = new DefaultArgumentInfoFactory();
        this.argmousService = new ArgmousServiceImpl();
        this.target = target;
    }

    public ParamCheckInvocationHandler(ValidationRuleFactory validationRuleFactory, ArgumentInfoFactory argumentInfoFactory, ArgmousService argmousService, Object target) {
        this.validationRuleFactory = validationRuleFactory;
        this.argumentInfoFactory = argumentInfoFactory;
        this.argmousService = argmousService;
        this.target = target;
    }

    public void setRuleMixHandler(RuleMixHandler ruleMixHandler) {
        this.ruleMixHandler = ruleMixHandler;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws ParamCheckException, InvocationTargetException, IllegalAccessException {
        Method targetMethod = method;
        try {
            targetMethod = target.getClass().getMethod(method.getName(), method.getParameterTypes());
        } catch (NoSuchMethodException ignored) {}
        Collection<ArgumentInfo> argumentInfos = argumentInfoFactory.createFromMethod(targetMethod, args);
        Collection<ValidationRule> validationRules = new LinkedList<>();
        ParamCheck single = targetMethod.getAnnotation(ParamCheck.class);
        ParamChecks multi = targetMethod.getAnnotation(ParamChecks.class);
        ArrayParamCheck array = targetMethod.getAnnotation(ArrayParamCheck.class);
        List<ValidationRule> beanRules = argumentInfos.stream()
                .flatMap(ri -> validationRuleFactory.createFromBean(ri.getValue(), ri.getName()).stream())
                .collect(Collectors.toList());
        if (single != null) {
            validationRules.add(validationRuleFactory.createFromAnnotation(single));
            argmousService.paramCheck(argumentInfos, ruleMixHandler.mix(beanRules, validationRules));
        } else if (multi != null) {
            validationRules.addAll(validationRuleFactory.createFromAnnotations(multi.value()));
            argmousService.paramCheck(argumentInfos, ruleMixHandler.mix(beanRules, validationRules));
        } else if (array != null) {
            validationRuleFactory.createFromAnnotations(array.value())
                    .forEach(i->{
                        i.setTarget(array.target());
                        validationRules.add(i);
                    });
            argmousService.arrayParamCheck(argumentInfos, ruleMixHandler.mix(beanRules, validationRules), array.target());
        } else if (!beanRules.isEmpty()) {
            argmousService.paramCheck(argumentInfos, beanRules);
        }
        return method.invoke(target, args);
    }
}
