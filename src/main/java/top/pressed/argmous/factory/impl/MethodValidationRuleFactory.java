package top.pressed.argmous.factory.impl;

import top.pressed.argmous.StandardInstanceBean;
import top.pressed.argmous.annotation.ArrayParamCheck;
import top.pressed.argmous.annotation.ParamCheck;
import top.pressed.argmous.annotation.ParamChecks;
import top.pressed.argmous.annotation.factory.OverrideTo;
import top.pressed.argmous.exception.RuleCreateException;
import top.pressed.argmous.exception.StandardInitException;
import top.pressed.argmous.factory.ValidationRuleFactory;
import top.pressed.argmous.handler.RuleAnnotationProcessor;
import top.pressed.argmous.manager.GetInstance;
import top.pressed.argmous.model.ValidationRule;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.rmi.NoSuchObjectException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

@OverrideTo(BeanValidationRuleFactory.class)
public class MethodValidationRuleFactory implements ValidationRuleFactory, StandardInstanceBean {

    public static String ARRAY_SELF_RULE = "ARGMOUS.isSelfRule";

    private RuleAnnotationProcessor annotationProcessor;
    private final Collection<Class<? extends Annotation>>
            supportAnnotations = Arrays.asList(ParamCheck.class, ParamChecks.class, ArrayParamCheck.class);

    @Override
    public Collection<ValidationRule> create(Method method, Object[] values, String[] argNames, boolean ignoreArray) throws RuleCreateException {
        Annotation annotation;
        for (Class<? extends Annotation> aClass : supportAnnotations) {
            annotation = method.getAnnotation(aClass);
            if (annotation != null) {
                Collection<ValidationRule> rules = annotationProcessor.process(annotation);
                rules.forEach(r -> {
                    if (r.getTarget().isEmpty()) {
                        r.setTarget(argNames[0]);
                    }
                    if (r.getCustom().contains(ARRAY_SELF_RULE)) {
                        r.addInclude(argNames[0]);
                    }
                });
                return rules;
            }
        }
        return Collections.emptyList();
    }

    @Override
    public void afterInitialize(GetInstance getter) throws StandardInitException {
        try {
            if (annotationProcessor == null) {
                annotationProcessor = getter.getInstance(RuleAnnotationProcessor.class);
            }
        } catch (NoSuchObjectException e) {
            throw new StandardInitException(e);
        }
    }
}
