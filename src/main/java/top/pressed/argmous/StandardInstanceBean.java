package top.pressed.argmous;

import top.pressed.argmous.exception.StandardInitException;
import top.pressed.argmous.manager.GetInstance;

public interface StandardInstanceBean {
    default void afterInitialize() throws StandardInitException {
    }

    default void afterInitialize(GetInstance getter) throws StandardInitException {
    }
}
