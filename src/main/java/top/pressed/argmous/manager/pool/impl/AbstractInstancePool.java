package top.pressed.argmous.manager.pool.impl;

import top.pressed.argmous.StandardInitBean;
import top.pressed.argmous.manager.pool.InstancePoolManager;

import java.rmi.NoSuchObjectException;

public abstract class AbstractInstancePool implements InstancePoolManager, StandardInitBean {

    protected abstract void addToPool(Object o, Class<?> type);

    public abstract Object getFromPool(Class<?> type);

    protected void afterSet() {
    }

    @Override
    @SuppressWarnings("unchecked")
    public synchronized <T> T getInstance(Class<T> type) throws NoSuchObjectException {
        T res = (T) getFromPool(type);
        if (res != null) {
            return res;
        } else {
            throw new NoSuchObjectException("no instance of " + type.getName());
        }
    }

    @Override
    public void setInstance(Object instance) throws IllegalArgumentException {
        if (instance == null) {
            throw new IllegalArgumentException("instance can't be null");
        }
        Class<?> insClass = instance.getClass();
        addToPool(instance, insClass);
        for (Class<?> i : insClass.getInterfaces()) {
            addToPool(instance, i);
        }
        Class<?> superClass = insClass.getSuperclass();
        while (superClass != null && !superClass.equals(Object.class)) {
            for (Class<?> i : superClass.getInterfaces()) {
                addToPool(instance, i);
            }
            addToPool(instance, superClass);
            superClass = superClass.getSuperclass();
        }
        afterSet();
    }
}
