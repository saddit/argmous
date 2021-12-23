package top.pressed.argmous.service;

import top.pressed.argmous.exception.ParamCheckException;

import java.lang.reflect.Method;

public interface ArgmousService {
    void startValidate(Method method, Object[] values, String[] names) throws ParamCheckException;
}
