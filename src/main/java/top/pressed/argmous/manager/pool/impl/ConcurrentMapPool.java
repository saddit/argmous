package top.pressed.argmous.manager.pool.impl;

import top.pressed.argmous.StandardInitBean;
import top.pressed.argmous.exception.StandardInitException;

import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentSkipListMap;

public class ConcurrentMapPool extends AbstractInstancePool {
    protected ConcurrentMap<String, Object> pool = new ConcurrentSkipListMap<>();

    @Override
    public void afterInitialize() throws StandardInitException {
        pool.values().forEach(i -> {
            if (i instanceof StandardInitBean) {
                ((StandardInitBean) i).afterInitialize();
            }
        });
    }

    @Override
    public Object getFromPool(Class<?> type) {
        return pool.get(type.getName());
    }

    @Override
    public void addToPool(Object o, Class<?> type) {
        pool.put(type.getName(), o);
    }

    @Override
    protected void afterSet() {
        pool.remove(StandardInitBean.class.getName());
    }
}
