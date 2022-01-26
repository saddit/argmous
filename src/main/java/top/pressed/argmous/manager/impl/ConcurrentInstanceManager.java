package top.pressed.argmous.manager.impl;

import top.pressed.argmous.StandardInstanceBean;
import top.pressed.argmous.exception.StandardInitException;

import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentSkipListMap;

public class ConcurrentInstanceManager extends AbstractInstanceManager {
    protected ConcurrentMap<String, Object> pool = new ConcurrentSkipListMap<>();

    @Override
    public void afterInitialize() throws StandardInitException {
        pool.values().forEach(i -> {
            if (i instanceof StandardInstanceBean) {
                ((StandardInstanceBean) i).afterInitialize();
                ((StandardInstanceBean) i).afterInitialize(this);
            }
        });
    }

    @Override
    public void clear() {
        pool.clear();
    }

    @Override
    public Object getFromPool(Class<?> type) {
        return pool.get(type.getName());
    }

    @Override
    public void addToPool(Object o, Class<?> type) {
        pool.put(type.getName(), o);
    }
}
