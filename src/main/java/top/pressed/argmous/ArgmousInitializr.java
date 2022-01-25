package top.pressed.argmous;

import lombok.experimental.UtilityClass;
import top.pressed.argmous.factory.ArgmousProxyFactory;
import top.pressed.argmous.factory.impl.*;
import top.pressed.argmous.handler.RuleAnnotationProcessor;
import top.pressed.argmous.handler.impl.TopologyMixingHandler;
import top.pressed.argmous.manager.InstanceManager;
import top.pressed.argmous.manager.ValidatorInject;
import top.pressed.argmous.manager.impl.DefaultValidationManager;
import top.pressed.argmous.manager.impl.DefaultValidatorManager;
import top.pressed.argmous.service.impl.ArgmousServiceImpl;
import top.pressed.argmous.validator.RuleValidator;

import java.rmi.NoSuchObjectException;
import java.util.Arrays;
import java.util.Collection;

@UtilityClass
public class ArgmousInitializr {
    private boolean isInit = false;

    public void addValidators(RuleValidator... validators) {
        addValidators(Arrays.asList(validators));
    }

    public void addValidators(Collection<RuleValidator> validators) {
        if (!isInit) {
            throw new IllegalStateException("Argmous should initialize before adding validators");
        }
        try {
            ValidatorInject vm = InstanceManager.instance().getInstance(ValidatorInject.class);
            if (vm != null) {
                vm.addValidators(validators);
            }
        } catch (NoSuchObjectException e) {
            e.printStackTrace();
        }
    }

    public void initBean(Object bean) {
        if (isInit) {
            throw new IllegalStateException("Argmous initialization has been completed");
        }
        InstanceManager.instance().setInstance(bean);
    }

    public void finishInit() {
        InstanceManager.instance().afterInitialize();
        isInit = true;
    }

    public void injectDefaultInstance() {
        InstanceManager pm = InstanceManager.instance();
        pm.setInstance(new RuleAnnotationProcessor());
        pm.setInstance(new CompositeRuleFactory());
        pm.setInstance(new MethodValidationRuleFactory());
        pm.setInstance(new BeanValidationRuleFactory());
        pm.setInstance(new SimpleArgumentInfoFactory());
        pm.setInstance(new DefaultValidatorManager());
        pm.setInstance(new DefaultValidationManager());
        pm.setInstance(new ArgmousServiceImpl());
        pm.setInstance(new TopologyMixingHandler());
        pm.setInstance(new JDKProxyFactory());
    }

    public void defaultInit() {
        if (isInit) return;
        injectDefaultInstance();
        finishInit();
    }

    public boolean hasInit() {
        return isInit;
    }

    public ArgmousProxyFactory getProxyFactory() {
        if (!isInit) {
            defaultInit();
        }
        try {
            return InstanceManager.instance().getInstance(ArgmousProxyFactory.class);
        } catch (NoSuchObjectException e) {
            throw new IllegalStateException(e);
        }
    }
}
