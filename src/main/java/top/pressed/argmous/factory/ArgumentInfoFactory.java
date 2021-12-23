package top.pressed.argmous.factory;

import top.pressed.argmous.exception.ArgumentCreateException;
import top.pressed.argmous.model.ArgumentInfo;

import java.lang.reflect.Method;
import java.util.Collection;

public interface ArgumentInfoFactory {

    /**
     * @param method      method
     * @param values      argument values
     * @param names       argument names
     * @param ignoreArray array supported
     * @return argument infos
     * @throws ArgumentCreateException if something wrong
     */
    Collection<ArgumentInfo> create(Method method, Object[] values, String[] names, boolean ignoreArray) throws ArgumentCreateException;

}
