package top.pressed.argmous;

import lombok.experimental.UtilityClass;
import top.pressed.argmous.factory.ArgmousProxyFactory;
import top.pressed.argmous.factory.impl.CompositeRuleFactory;
import top.pressed.argmous.factory.impl.JDKProxyFactory;
import top.pressed.argmous.factory.impl.SimpleArgumentInfoFactory;
import top.pressed.argmous.handler.RuleAnnotationProcessor;
import top.pressed.argmous.handler.impl.TopologyMixingHandler;
import top.pressed.argmous.manager.pool.InstancePoolManager;
import top.pressed.argmous.manager.validation.impl.DefaultValidationManager;
import top.pressed.argmous.manager.validator.ValidatorInject;
import top.pressed.argmous.manager.validator.impl.DefaultValidatorManager;
import top.pressed.argmous.service.impl.ArgmousServiceImpl;
import top.pressed.argmous.validator.RuleValidator;

import java.rmi.NoSuchObjectException;
import java.util.Arrays;
import java.util.Collection;

@UtilityClass
public class ArgmousInitializr {
    private boolean isInit = false;

    public void addValidators(RuleValidator... validators) {
        if (!isInit) {
            defaultInit();
        }
        try {
            ValidatorInject vm = InstancePoolManager.instance().getInstance(ValidatorInject.class);
            if (vm != null) {
                vm.addValidators(Arrays.asList(validators));
            }
        } catch (NoSuchObjectException e) {
            e.printStackTrace();
        }
    }

    public void addValidators(Collection<RuleValidator> validators) {
        if (!isInit) {
            defaultInit();
        }
        try {
            ValidatorInject vm = InstancePoolManager.instance().getInstance(ValidatorInject.class);
            if (vm != null) {
                vm.addValidators(validators);
            }
        } catch (NoSuchObjectException e) {
            e.printStackTrace();
        }
    }

    public void initBean(Object bean) {
        InstancePoolManager.instance().setInstance(bean);
        isInit = true;
    }

    public void finishInit() {
        ((StandardInitBean) InstancePoolManager.instance()).afterInitialize();
    }

    public void defaultInit() {
        InstancePoolManager pm = InstancePoolManager.instance();
        pm.setInstance(new RuleAnnotationProcessor());
        pm.setInstance(new CompositeRuleFactory());
        pm.setInstance(new SimpleArgumentInfoFactory());
        pm.setInstance(new DefaultValidatorManager());
        //required validator
        pm.setInstance(new DefaultValidationManager());
        //required validation and validator
        pm.setInstance(new ArgmousServiceImpl());
        //required by proxyHandler
        pm.setInstance(new TopologyMixingHandler());
        pm.setInstance(new JDKProxyFactory());
        isInit = true;
        finishInit();
    }

    public ArgmousProxyFactory getProxyFactory() {
        if (!isInit) {
            defaultInit();
        }
        try {
            return InstancePoolManager.instance().getInstance(ArgmousProxyFactory.class);
        } catch (NoSuchObjectException e) {
            throw new IllegalStateException(e);
        }
    }
}
