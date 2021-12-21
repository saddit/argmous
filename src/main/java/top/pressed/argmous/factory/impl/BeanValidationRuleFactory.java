package top.pressed.argmous.factory.impl;

import top.pressed.argmous.StandardInitBean;
import top.pressed.argmous.exception.RuleCreateException;
import top.pressed.argmous.exception.StandardInitException;
import top.pressed.argmous.factory.ValidationRuleFactory;
import top.pressed.argmous.handler.RuleAnnotationProcessor;
import top.pressed.argmous.manager.pool.InstancePoolManager;
import top.pressed.argmous.model.ValidationRule;
import top.pressed.argmous.util.BeanUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.rmi.NoSuchObjectException;
import java.util.ArrayList;
import java.util.Collection;

public class BeanValidationRuleFactory implements ValidationRuleFactory, StandardInitBean {

    private RuleAnnotationProcessor annotationProcessor;

    @Override
    public Collection<ValidationRule> create(Method method, String[] argNames, boolean ignoreArray) throws RuleCreateException {
        Parameter[] parameters = method.getParameters();
        Collection<ValidationRule> rules = new ArrayList<>();
        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];
            Class<?> type = parameter.getType();
            if (BeanUtils.isBean(type)) {
                String name = argNames[i];
                Field[] fields = type.getDeclaredFields();
                boolean shouldValid = false;
                for (Field field : fields) {
                    ValidationRule rule = ValidationRule.empty();
                    rule.setTarget(name);
                    rule.addInclude(field.getName());
                    for (Annotation annotation : field.getDeclaredAnnotations()) {
                        if (annotationProcessor.processBeanAnnotation(annotation, rule)) {
                            shouldValid = true;
                        }
                    }
                    if (shouldValid) {
                        rules.add(rule);
                    }
                }
            }
        }
        return rules;
    }

    @Override
    public void afterInitialize() throws StandardInitException {
        try {
            if (annotationProcessor == null) {
                annotationProcessor = InstancePoolManager.instance().getInstance(RuleAnnotationProcessor.class);
            }
        } catch (NoSuchObjectException e) {
            throw new StandardInitException(e);
        }
    }
}
