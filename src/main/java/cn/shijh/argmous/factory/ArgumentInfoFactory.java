package cn.shijh.argmous.factory;

import cn.shijh.argmous.exception.ArgmousBaseException;
import cn.shijh.argmous.exception.ArgumentCreateException;
import cn.shijh.argmous.model.ArgumentInfo;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Collection;

public interface ArgumentInfoFactory {

    ArgumentInfo createFromArg(Object arg, Parameter p) throws ArgumentCreateException;

    Collection<ArgumentInfo> createFromFields(Object arg, String name, Class<?> clazz) throws ArgumentCreateException;

    Collection<ArgumentInfo> createFromMethod(Method method, Object[] args) throws ArgumentCreateException;

    Collection<ArgumentInfo> createFromArray(Collection<?> objects, String name) throws ArgumentCreateException;
}
