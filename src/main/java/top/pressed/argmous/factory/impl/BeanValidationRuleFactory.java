package top.pressed.argmous.factory.impl;

import top.pressed.argmous.StandardInstanceBean;
import top.pressed.argmous.exception.RuleCreateException;
import top.pressed.argmous.exception.StandardInitException;
import top.pressed.argmous.factory.ValidationRuleFactory;
import top.pressed.argmous.handler.RuleAnnotationProcessor;
import top.pressed.argmous.manager.InstanceManager;
import top.pressed.argmous.model.ValidationRule;
import top.pressed.argmous.util.BeanUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.rmi.NoSuchObjectException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

public class BeanValidationRuleFactory implements ValidationRuleFactory, StandardInstanceBean {

    private RuleAnnotationProcessor annotationProcessor;

    public Collection<ValidationRule> createFromBean(Class<?> type, String name) {
        Field[] fields = type.getDeclaredFields();
        boolean shouldValid = false;
        Collection<ValidationRule> rules = new ArrayList<>(fields.length);
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
        return rules;
    }

    protected void onCreate(Class<?> type, Collection<ValidationRule> rules) {

    }

    protected boolean preCreate(Class<?> type, Collection<ValidationRule> rules) {
        return true;
    }

    @Override
    public Collection<ValidationRule> create(Method method, Object[] values, String[] argNames, boolean ignoreArray) throws RuleCreateException {
        Parameter[] parameters = method.getParameters();
        Collection<ValidationRule> rules = new ArrayList<>();
        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];
            Class<?> type = parameter.getType();
            if (BeanUtils.isBean(type)) {
                if (preCreate(type, rules)) {
                    Collection<ValidationRule> fromBean = createFromBean(type, argNames[i]);
                    rules.addAll(fromBean);
                    onCreate(type, fromBean);
                }
            } else if (!ignoreArray) {
                Object value = values[i];
                if (value instanceof Collection) {
                    Optional<?> first = ((Collection<?>) value).stream().findFirst();
                    if (first.isPresent()) {
                        Class<?> elemClass = first.get().getClass();
                        if (BeanUtils.isBean(elemClass)) {
                            if (preCreate(elemClass, rules)) {
                                Collection<ValidationRule> fromBean = createFromBean(elemClass, argNames[i]);
                                rules.addAll(fromBean);
                                onCreate(elemClass, fromBean);
                            }
                        }
                    }
                } else if (value != null && type.isArray()) {
                    Class<?> elemClass = type.getComponentType();
                    if (BeanUtils.isBean(elemClass)) {
                        if (preCreate(elemClass, rules)) {
                            Collection<ValidationRule> fromBean = createFromBean(elemClass, argNames[i]);
                            rules.addAll(fromBean);
                            onCreate(elemClass, fromBean);
                        }
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
                annotationProcessor = InstanceManager.instance().getInstance(RuleAnnotationProcessor.class);
            }
        } catch (NoSuchObjectException e) {
            throw new StandardInitException(e);
        }
    }
}
