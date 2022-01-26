package top.pressed.argmous.factory.impl;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import top.pressed.argmous.StandardInstanceBean;
import top.pressed.argmous.exception.StandardInitException;
import top.pressed.argmous.factory.ArgmousProxyFactory;
import top.pressed.argmous.handler.impl.ParamCheckProxyHandler;
import top.pressed.argmous.manager.GetInstance;
import top.pressed.argmous.service.ArgmousService;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Proxy;
import java.rmi.NoSuchObjectException;

@AllArgsConstructor
@NoArgsConstructor
public class JDKProxyFactory implements ArgmousProxyFactory, StandardInstanceBean {

    private ArgmousService service;

    @Override
    public Object proxy(Object target) {
        Class<?> clazz = target.getClass();
        try {
            return Proxy.newProxyInstance(clazz.getClassLoader(), clazz.getInterfaces(),
                    new ParamCheckProxyHandler(target, service));
        } catch (NoSuchObjectException e) {
            throw new IllegalStateException(e);
        }
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

    @Override
    public void afterInitialize(GetInstance getter) throws StandardInitException {
        try {
            if (service == null) {
                this.service = getter.getInstance(ArgmousService.class);
            }
        } catch (NoSuchObjectException e) {
            throw new StandardInitException(e);
        }
    }
}
