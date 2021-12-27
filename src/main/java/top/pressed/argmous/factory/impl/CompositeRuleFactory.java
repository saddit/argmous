package top.pressed.argmous.factory.impl;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import top.pressed.argmous.StandardInstanceBean;
import top.pressed.argmous.annotation.factory.InstancePriority;
import top.pressed.argmous.exception.RuleCreateException;
import top.pressed.argmous.exception.StandardInitException;
import top.pressed.argmous.factory.ValidationRuleFactory;
import top.pressed.argmous.handler.RuleMixHandler;
import top.pressed.argmous.manager.InstanceManager;
import top.pressed.argmous.model.ValidationRule;

import java.lang.reflect.Method;
import java.rmi.NoSuchObjectException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@InstancePriority(Integer.MAX_VALUE)
public class CompositeRuleFactory implements ValidationRuleFactory, StandardInstanceBean {

    private Collection<ValidationRuleFactory> factories;
    private RuleMixHandler ruleMixHandler;

    public CompositeRuleFactory(Collection<ValidationRuleFactory> factories) {
        this.factories = factories;
    }

    @Override
    public Collection<ValidationRule> create(Method method, Object[] values, String[] argNames, boolean ignoreArray) throws RuleCreateException {
        Map<Class<? extends ValidationRuleFactory>, Collection<ValidationRule>> dataMap = new HashMap<>(factories.size());

        for (ValidationRuleFactory factory : factories) {
            Collection<ValidationRule> rules = factory.create(method, values, argNames, ignoreArray);
            dataMap.put(factory.getClass(), rules);
        }

        return ruleMixHandler.handle(dataMap);
    }

    @Override
    public void afterInitialize() throws StandardInitException {
        try {
            InstanceManager pool = InstanceManager.instance();
            if (ruleMixHandler == null) {
                ruleMixHandler = pool.getInstance(RuleMixHandler.class);
            }
            if (factories == null) {
                factories = Arrays.asList(
                        pool.getInstance(BeanValidationRuleFactory.class),
                        pool.getInstance(MethodValidationRuleFactory.class)
                );
            }
        } catch (NoSuchObjectException e) {
            throw new StandardInitException(e);
        }
    }
}
