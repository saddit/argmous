package top.pressed.argmous.manager.impl;

import top.pressed.argmous.StandardInstanceBean;
import top.pressed.argmous.annotation.factory.InstancePriority;
import top.pressed.argmous.exception.InstanceException;
import top.pressed.argmous.manager.InstanceManager;

import java.rmi.NoSuchObjectException;


public abstract class AbstractInstanceManager implements InstanceManager {

    protected abstract void addToPool(Object o, Class<?> type);

    public abstract Object getFromPool(Class<?> type);

    protected void afterSet() {
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getInstance(Class<T> type) throws NoSuchObjectException {
        T res = (T) getFromPool(type);
        if (res != null) {
            return res;
        } else {
            throw new NoSuchObjectException("no instance of " + type.getName());
        }
    }

    protected final int getPriority(Class<?> clazz) {
        InstancePriority annotation = clazz.getAnnotation(InstancePriority.class);
        return annotation == null ? -1 : annotation.value();
    }

    protected boolean allowSet(Class<?> type, Object instance) {
        if (StandardInstanceBean.class.equals(type)) {
            return false;
        }
        Object o = getFromPool(type);
        if (o != null) {
            int oldI = getPriority(o.getClass());
            int newI = getPriority(instance.getClass());
            return oldI < newI;
        }
        return true;
    }

    private void singletonValidate(Class<?> type) {
        if (getFromPool(type) != null) {
            throw new InstanceException("duplicated instance of " + type.getName());
        }
    }

    @Override
    public void setInstance(Object instance) throws InstanceException {
        if (instance == null) {
            throw new IllegalArgumentException("instance can't be null");
        }
        Class<?> insClass = instance.getClass();
        singletonValidate(insClass);
        addToPool(instance, insClass);
        while (insClass != null) {
            for (Class<?> i : insClass.getInterfaces()) {
                if (allowSet(i, instance)) {
                    addToPool(instance, i);
                }
            }
            insClass = insClass.getSuperclass();
        }
        afterSet();
    }
}
