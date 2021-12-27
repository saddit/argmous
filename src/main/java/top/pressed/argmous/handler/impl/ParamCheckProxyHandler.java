package top.pressed.argmous.handler.impl;

import top.pressed.argmous.annotation.Valid;
import top.pressed.argmous.manager.InstanceManager;
import top.pressed.argmous.service.ArgmousService;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.rmi.NoSuchObjectException;

public class ParamCheckProxyHandler implements InvocationHandler {

    private Object target;
    private ArgmousService service;

    public ParamCheckProxyHandler(Object target) throws NoSuchObjectException {
        this.target = target;
        service = InstanceManager.instance().getInstance(ArgmousService.class);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Method targetMethod = method;
        try {
            targetMethod = target.getClass().getMethod(method.getName(), method.getParameterTypes());
        } catch (NoSuchMethodException ignored) {
        }
        service.startValidate(targetMethod, args, generateNames(targetMethod));
        return method.invoke(target, args);
    }

    private String[] generateNames(Method method) {
        Parameter[] parameters = method.getParameters();
        String[] names = new String[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            Valid valid = parameters[i].getAnnotation(Valid.class);
            names[i] = valid == null ? parameters[i].getName() : valid.value();
        }
        return names;
    }
}
