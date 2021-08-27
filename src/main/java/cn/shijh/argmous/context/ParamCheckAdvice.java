package cn.shijh.argmous.context;


import cn.shijh.argmous.exception.ParamCheckException;
import cn.shijh.argmous.validator.RuleValidator;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Aspect
@Component
@Slf4j
@Order(2)
public class ParamCheckAdvice {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private List<RuleValidator> validators;

    @Pointcut("@annotation(cn.shijh.argmous.context.ParamCheck)")
    public void pointCut() {
    }

    @Pointcut("@annotation(cn.shijh.argmous.context.ParamChecks)")
    public void multiParamCheck() {
    }

    @Pointcut("@annotation(cn.shijh.argmous.context.ArrayParamCheck)")
    public void arrayParamChecks() {
    }

    private <T extends Annotation> T getAnnotation(JoinPoint jp, Class<T> targetClass) {
        return ((MethodSignature) jp.getSignature()).getMethod().getAnnotation(targetClass);
    }

    private boolean isTarget(ParamCheck rule, String argName) {
        return rule.target().isEmpty() || rule.target().equals(argName);
    }

    private boolean isAllowCheck(ParamCheck anno, String paramName) {
        if (anno.include().length > 0) {
            return Arrays.asList(anno.include()).contains(paramName);
        } else if (anno.exclude().length > 0) {
            return !Arrays.asList(anno.exclude()).contains(paramName);
        }
        return true;
    }

    private boolean isSpringObject(Object arg) {
        if (arg == null) return false;
        try {
            applicationContext.getBean(arg.getClass());
            return true;
        } catch (BeansException e) {
            return false;
        }
    }

    private void throwException(String argName, String message) {
        if (message == null) {
            message = "Unknown Error";
        }
        throw new ParamCheckException(argName + ":" + message);
    }

    /**
     * use validator to check
     * @param param need to check
     * @param anno rule
     * @return error message if param no valid else null
     * @throws IllegalStateException if something got wrong
     */
    @Nullable
    private String isWrongParam(Object param, ParamCheck anno) throws IllegalStateException{
        for (RuleValidator v : validators) {
            if (v.support(param.getClass(), anno) && !v.validate(param, anno)) {
                return v.errorMessage(anno);
            }
        }
        return null;
    }

    private boolean hasAllowedField(Class<?> clazz, ParamCheck anno) {
        for (Field field : clazz.getDeclaredFields()) {
            if (isAllowCheck(anno, field.getName())) {
                return true;
            }
        }
        return false;
    }

    private Collection<Field> getAllowedFields(Class<?> clazz, ParamCheck rule) {
        Collection<Field> allowedFields;
        if(rule.include().length > 0) {
            allowedFields = new ArrayList<>(rule.include().length);
            for (String s : rule.include()) {
                try {
                    allowedFields.add(clazz.getDeclaredField(s));
                } catch (NoSuchFieldException ignore) { }
            }
        } else {
            allowedFields = Arrays.stream(clazz.getDeclaredFields())
                    .filter(f->isAllowCheck(rule,f.getName()))
                    .collect(Collectors.toList());
        }
        return allowedFields;
    }

    private void fieldCheck(Object arg, ParamCheck rule) throws ParamCheckException, IllegalAccessException {
        for (Field field : getAllowedFields(arg.getClass(),rule)) {
            field.setAccessible(true);
            String res = isWrongParam(field.get(arg), rule);
            if (res != null) {
                throwException(field.getName(),res);
            }
        }
    }

    private void doSingleCheck(@Nullable Object arg, Parameter parameter, Class<?> argType,
                              String argName, ParamCheck rule) throws ParamCheckException, IllegalAccessException {
        //skip irrelevant arg
        if (isSpringObject(arg) || !isTarget(rule, argName)) {
            return;
        }
        //if directly check arg
        if (isAllowCheck(rule, argName)) {
            String res = isWrongParam(arg, rule);
            if (res != null) {
                throwException(argName, res);
            }
        }
        //check fields of the arg
        if (arg != null) {
            fieldCheck(arg,rule);
        } else if (parameter != null && hasAllowedField(argType, rule)) {
            //if arg is null but need to check
            throwException( argName,"required");
        }
    }

    private void doParamCheck(Object[] args, Class<?>[] argTypes,
                             String[] argNames, Parameter[] parameters,
                             ParamCheck rule) throws ParamCheckException, IllegalAccessException {
        for (int i = 0; i < args.length; i++) {
            Object arg = args[i];
            String argName = argNames[i];
            Class<?> argType = argTypes[i];
            doSingleCheck(arg, parameters[i], argType, argName, rule);
        }
    }

    @Before(value = "pointCut()")
    public void paramCheck(JoinPoint jp) throws ParamCheckException, IllegalAccessException {
        Object[] args = jp.getArgs();
        MethodSignature signature = (MethodSignature) jp.getSignature();
        Class<?>[] argTypes = signature.getParameterTypes();
        String[] argNames = signature.getParameterNames();
        Parameter[] parameters = signature.getMethod().getParameters();
        ParamCheck rule = getAnnotation(jp, ParamCheck.class);
        doParamCheck(args, argTypes, argNames, parameters, rule);
    }

    @Before(value = "multiParamCheck()")
    public void paramChecks(JoinPoint jp) throws ParamCheckException, IllegalAccessException {
        Object[] args = jp.getArgs();
        MethodSignature signature = (MethodSignature) jp.getSignature();
        Class<?>[] argTypes = signature.getParameterTypes();
        String[] argNames = signature.getParameterNames();
        Parameter[] parameters = signature.getMethod().getParameters();
        ParamChecks rules = getAnnotation(jp, ParamChecks.class);
        for (ParamCheck rule : rules.value()) {
            doParamCheck(args, argTypes, argNames, parameters, rule);
        }
    }

    @Before(value = "arrayParamChecks()")
    public void arrayParamChecks(JoinPoint jp) throws ParamCheckException, IllegalAccessException {
        Object[] args = jp.getArgs();
        MethodSignature signature = (MethodSignature) jp.getSignature();
        String[] argNames = signature.getParameterNames();
        ArrayParamCheck rules = getAnnotation(jp, ArrayParamCheck.class);
        for (int i = 0; i<args.length; i++) {
            Object arg = args[i];
            String argName = argNames[i];
            if (arg instanceof Collection) {
                if(!rules.target().equals(argName)) {
                    continue;
                }
                if(rules.required() && ((Collection) arg).isEmpty()) {
                    throwException(argName,"required");
                }
                for (Object item : (Collection<?>) arg) {
                    for (ParamCheck rule : rules.value()) {
                        doSingleCheck(item, null, null, null, rule);
                    }
                }
            }
        }
    }

}
