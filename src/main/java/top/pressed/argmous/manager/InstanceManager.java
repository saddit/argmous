package top.pressed.argmous.manager;

import top.pressed.argmous.StandardInstanceBean;
import top.pressed.argmous.exception.InstanceException;
import top.pressed.argmous.manager.impl.ConcurrentInstanceManager;

import java.rmi.NoSuchObjectException;

public interface InstanceManager extends StandardInstanceBean, GetInstance {
    <T> T getInstance(Class<T> type) throws NoSuchObjectException;

    void setInstance(Object instance) throws InstanceException;

    void clear();

    @Deprecated
    class InstanceHolder {
        private static InstanceManager DEFAULT = null;
    }

    @Deprecated
    static InstanceManager instance() {
        if (InstanceHolder.DEFAULT == null) {
            InstanceHolder.DEFAULT = new ConcurrentInstanceManager();
        }
        return InstanceHolder.DEFAULT;
    }
}
