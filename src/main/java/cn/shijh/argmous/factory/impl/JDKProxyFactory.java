package cn.shijh.argmous.factory.impl;

import cn.shijh.argmous.factory.ArgmousProxyFactory;
import cn.shijh.argmous.factory.ArgumentInfoFactory;
import cn.shijh.argmous.factory.ValidationRuleFactory;
import cn.shijh.argmous.factory.impl.DefaultArgumentInfoFactory;
import cn.shijh.argmous.factory.impl.DefaultValidationRuleFactory;
import cn.shijh.argmous.handler.impl.ParamCheckInvocationHandler;
import cn.shijh.argmous.service.ArgmousService;
import cn.shijh.argmous.service.impl.ArgmousServiceImpl;
import lombok.AllArgsConstructor;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Proxy;

@AllArgsConstructor
public class JDKProxyFactory implements ArgmousProxyFactory {
    private final ValidationRuleFactory validationRuleFactory;
    private final ArgumentInfoFactory argumentInfoFactory;
    private final ArgmousService argmousService;

    public JDKProxyFactory() {
        validationRuleFactory = new DefaultValidationRuleFactory();
        argumentInfoFactory = new DefaultArgumentInfoFactory();
        argmousService = new ArgmousServiceImpl();
    }

    @Override
    public Object proxy(Object target) {
        Class<?> clazz = target.getClass();
        return Proxy.newProxyInstance(clazz.getClassLoader(), clazz.getInterfaces(),
                new ParamCheckInvocationHandler(validationRuleFactory,argumentInfoFactory, argmousService, target));
    }

    @Override
    public Object newProxyInstance(Class<?> instanceClass) {
        try {
            Constructor<?> constructor = instanceClass.getConstructor();
            Object o = constructor.newInstance();
            return proxy(o);
        } catch (NoSuchMethodException e) {
            throw new IllegalStateException("error init " + instanceClass.getName() + ", hadn't constructor of empty");
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
            throw new IllegalStateException(e);
        }
    }
}
