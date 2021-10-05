package top.pressed.argmous.handler.impl;

import top.pressed.argmous.annotation.ArrayParamCheck;
import top.pressed.argmous.annotation.ParamCheck;
import top.pressed.argmous.annotation.ParamChecks;
import top.pressed.argmous.exception.ParamCheckException;
import top.pressed.argmous.factory.ArgumentInfoFactory;
import top.pressed.argmous.factory.ValidationRuleFactory;
import top.pressed.argmous.factory.impl.DefaultArgumentInfoFactory;
import top.pressed.argmous.factory.impl.DefaultValidationRuleFactory;
import top.pressed.argmous.handler.RuleMixHandler;
import top.pressed.argmous.model.ArgumentInfo;
import top.pressed.argmous.model.ValidationRule;
import top.pressed.argmous.service.ArgmousService;
import top.pressed.argmous.service.impl.ArgmousServiceImpl;
import top.pressed.argmous.util.BeanUtils;

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
     * 需要进一步优化
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
                List<ArgumentInfo> arrayArgInfos = argumentInfos.stream()
                        .filter(i -> i.getValue() instanceof Collection)
                        .flatMap(
                                i -> argumentInfoFactory
                                        .createFromArray((Collection<?>) i.getValue(), i.getName()).stream()
                        )
                        .collect(Collectors.toList());
                validationRules.addAll(
                        validationRuleFactory.createFromAnnotation(array, defaultTargetName.get())
                );
                //add element's class rules
                argumentInfos.stream().findFirst()
                        .ifPresent(arg -> {
                            if (BeanUtils.isBean(arg.getType())) {
                                validationRules.addAll(validationRuleFactory.createFromBean(arg.getType(), defaultTargetName.get()));
                            }
                        });
                argumentInfos.addAll(arrayArgInfos);
                argmousService.paramCheck(argumentInfos, ruleMixHandler.mix(beanRules, validationRules));
            } else if (!beanRules.isEmpty()) {
                argmousService.paramCheck(argumentInfos, beanRules);
            }
        }
        return method.invoke(target, args);
    }
}
