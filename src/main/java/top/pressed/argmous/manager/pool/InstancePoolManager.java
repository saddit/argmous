package top.pressed.argmous.manager.pool;

import top.pressed.argmous.manager.pool.impl.ConcurrentMapPool;

import java.rmi.NoSuchObjectException;

public interface InstancePoolManager {
    <T> T getInstance(Class<T> type) throws NoSuchObjectException;

    void setInstance(Object instance) throws IllegalArgumentException;

    class InstanceHolder {
        private static InstancePoolManager DEFAULT = null;
    }

    static InstancePoolManager i() {
        if (InstanceHolder.DEFAULT == null) {
            InstanceHolder.DEFAULT = new ConcurrentMapPool();
        }
        return InstanceHolder.DEFAULT;
    }
}
