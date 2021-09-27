package cn.shijh.argmous.handler.impl;

import cn.shijh.argmous.annotation.ArrayParamCheck;
import cn.shijh.argmous.annotation.ParamCheck;
import cn.shijh.argmous.annotation.ParamChecks;
import cn.shijh.argmous.exception.ParamCheckException;
import cn.shijh.argmous.factory.ArgumentInfoFactory;
import cn.shijh.argmous.factory.ValidationRuleFactory;
import cn.shijh.argmous.factory.impl.DefaultArgumentInfoFactory;
import cn.shijh.argmous.factory.impl.DefaultValidationRuleFactory;
import cn.shijh.argmous.handler.RuleMixHandler;
import cn.shijh.argmous.model.ArgumentInfo;
import cn.shijh.argmous.model.ValidationRule;
import cn.shijh.argmous.service.ArgmousService;
import cn.shijh.argmous.service.impl.ArgmousServiceImpl;
import cn.shijh.argmous.util.BeanUtils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
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

    /**
     *  需要进一步优化
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws ParamCheckException, InvocationTargetException, IllegalAccessException {
        Method targetMethod = method;
        try {
            targetMethod = target.getClass().getMethod(method.getName(), method.getParameterTypes());
        } catch (NoSuchMethodException ignored) {
        }
        Collection<ArgumentInfo> argumentInfos = argumentInfoFactory.createFromMethod(targetMethod, args);
        if (!argumentInfos.isEmpty()) {
            AtomicReference<String> defaultTargetName = new AtomicReference<>("");
            ParamCheck single = targetMethod.getAnnotation(ParamCheck.class);
            ParamChecks multi = targetMethod.getAnnotation(ParamChecks.class);
            ArrayParamCheck array = targetMethod.getAnnotation(ArrayParamCheck.class);
            Collection<ValidationRule> validationRules = new LinkedList<>();
            List<ValidationRule> beanRules = argumentInfos.stream()
                    .peek(argumentInfo -> {
                        if (defaultTargetName.get().isEmpty()) {
                            defaultTargetName.set(argumentInfo.getName());
                        }
                    })
                    .filter(ri -> BeanUtils.isBean(ri.getType()))
                    .flatMap(ri -> validationRuleFactory.createFromBean(ri.getType(), ri.getName()).stream())
                    .collect(Collectors.toList());
            if (single != null) {
                validationRules.add(validationRuleFactory.createFromAnnotation(single, defaultTargetName.get()));
                argmousService.paramCheck(argumentInfos, ruleMixHandler.mix(beanRules, validationRules));
            } else if (multi != null) {
                validationRules.addAll(validationRuleFactory.createFromAnnotations(multi.value(), defaultTargetName.get()));
                argmousService.paramCheck(argumentInfos, ruleMixHandler.mix(beanRules, validationRules));
            } else if (array != null) {
                validationRules.addAll(
                        validationRuleFactory.createFromAnnotations(array.value(),
                                array.target().isEmpty() ? defaultTargetName.get() : array.target())
                );
                ValidationRule selfRule = validationRuleFactory.createFromAnnotation(array.self(),
                        array.target().isEmpty() ? defaultTargetName.get() : array.target());
                List<ArgumentInfo> arrayArgInfos = argumentInfos.stream()
                        .filter(i -> i.getValue() instanceof Collection)
                        .flatMap(
                                i -> argumentInfoFactory
                                        .createFromArray((Collection<?>) i.getValue(), i.getName()).stream()
                        )
                        .collect(Collectors.toList());
                argmousService.paramCheck(argumentInfos, Collections.singletonList(selfRule));
                argmousService.paramCheck(arrayArgInfos, ruleMixHandler.mix(beanRules, validationRules));
            } else if (!beanRules.isEmpty()) {
                argmousService.paramCheck(argumentInfos, beanRules);
            }
        }
        return method.invoke(target, args);
    }
}
