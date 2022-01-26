package top.pressed.argmous;

import top.pressed.argmous.factory.ArgmousProxyFactory;
import top.pressed.argmous.factory.impl.*;
import top.pressed.argmous.handler.RuleAnnotationProcessor;
import top.pressed.argmous.handler.impl.TopologyMixingHandler;
import top.pressed.argmous.manager.InstanceManager;
import top.pressed.argmous.manager.ValidatorInject;
import top.pressed.argmous.manager.impl.ConcurrentInstanceManager;
import top.pressed.argmous.manager.impl.DefaultValidationManager;
import top.pressed.argmous.manager.impl.DefaultValidatorManager;
import top.pressed.argmous.service.impl.ArgmousServiceImpl;
import top.pressed.argmous.validator.RuleValidator;

import java.rmi.NoSuchObjectException;
import java.util.Arrays;
import java.util.Collection;

public class ArgmousInitializr {
    private boolean isInit = false;
    private final InstanceManager instanceManager;

    @Deprecated
    private static ArgmousInitializr _instance;

    @Deprecated
    public static ArgmousInitializr getInstance() {
        if (_instance == null) {
            _instance = new ArgmousInitializr();
        }
        return _instance;
    }

    public ArgmousInitializr(InstanceManager instanceManager) {
        this.instanceManager = instanceManager;
    }

    public ArgmousInitializr() {
        this(new ConcurrentInstanceManager());
    }

    public void addValidators(RuleValidator... validators) {
        addValidators(Arrays.asList(validators));
    }

    public void addValidators(Collection<RuleValidator> validators) {
        if (!isInit) {
            throw new IllegalStateException("Argmous should initialize before adding validators");
        }
        try {
            ValidatorInject vm = getInstanceManager().getInstance(ValidatorInject.class);
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
        getInstanceManager().setInstance(bean);
    }

    public void finishInit() {
        getInstanceManager().afterInitialize();
        isInit = true;
    }

    public void injectDefaultInstance() {
        InstanceManager pm = getInstanceManager();
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
            return getInstanceManager().getInstance(ArgmousProxyFactory.class);
        } catch (NoSuchObjectException e) {
            throw new IllegalStateException(e);
        }
    }

    public InstanceManager getInstanceManager() {
        return instanceManager;
    }
}
