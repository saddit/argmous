package top.pressed.argmous.factory.impl;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import top.pressed.argmous.StandardInitBean;
import top.pressed.argmous.exception.RuleCreateException;
import top.pressed.argmous.exception.StandardInitException;
import top.pressed.argmous.factory.ValidationRuleFactory;
import top.pressed.argmous.handler.RuleMixHandler;
import top.pressed.argmous.manager.pool.InstancePoolManager;
import top.pressed.argmous.model.ValidationRule;

import java.lang.reflect.Method;
import java.rmi.NoSuchObjectException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
public class CompositeRuleFactory implements ValidationRuleFactory, StandardInitBean {

    private Collection<ValidationRuleFactory> factories;
    private RuleMixHandler ruleMixHandler;

    @Override
    public Collection<ValidationRule> create(Method method, String[] argNames, boolean ignoreArray) throws RuleCreateException {
        Map<Class<? extends ValidationRuleFactory>, Collection<ValidationRule>> dataMap = new HashMap<>(factories.size());

        for (ValidationRuleFactory factory : factories) {
            Collection<ValidationRule> rules = factory.create(method, argNames, ignoreArray);
            dataMap.put(factory.getClass(), rules);
        }

        return ruleMixHandler.handle(dataMap);
    }

    @Override
    public void afterInitialize() throws StandardInitException {
        try {
            if (ruleMixHandler == null) {
                ruleMixHandler = InstancePoolManager.instance().getInstance(RuleMixHandler.class);
            }
            if (factories == null) {
                factories = Arrays.asList(new BeanValidationRuleFactory(), new MethodValidationRuleFactory());
                factories.forEach(i -> {
                    if (i instanceof StandardInitBean) {
                        ((StandardInitBean) i).afterInitialize();
                    }
                });
            }
        } catch (NoSuchObjectException e) {
            throw new StandardInitException(e);
        }
    }
}
