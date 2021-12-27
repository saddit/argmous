package top.pressed.argmous;

import top.pressed.argmous.exception.StandardInitException;

public interface StandardInstanceBean {
    default void afterInitialize() throws StandardInitException {
    }
}
