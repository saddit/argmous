package top.pressed.argmous;

import top.pressed.argmous.exception.StandardInitException;

public interface StandardInitBean {
    default void afterInitialize() throws StandardInitException {
    }
}
