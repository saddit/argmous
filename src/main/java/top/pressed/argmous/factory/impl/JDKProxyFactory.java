package top.pressed.argmous.factory.impl;

import lombok.AllArgsConstructor;
import top.pressed.argmous.StandardInitBean;
import top.pressed.argmous.factory.ArgmousProxyFactory;
import top.pressed.argmous.handler.impl.ParamCheckProxyHandler;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Proxy;
import java.rmi.NoSuchObjectException;

@AllArgsConstructor
public class JDKProxyFactory implements ArgmousProxyFactory, StandardInitBean {

    @Override
    public Object proxy(Object target) {
        Class<?> clazz = target.getClass();
        try {
            return Proxy.newProxyInstance(clazz.getClassLoader(), clazz.getInterfaces(),
                    new ParamCheckProxyHandler(target));
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
}
