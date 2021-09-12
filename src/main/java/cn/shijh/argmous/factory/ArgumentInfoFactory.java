package cn.shijh.argmous.factory;

import cn.shijh.argmous.model.ArgumentInfo;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Collection;

public interface ArgumentInfoFactory {

    ArgumentInfo createFromArg(Object arg, Parameter p);

    Collection<ArgumentInfo> createFromFields(Object arg, String name, Class<?> clazz);

    Collection<ArgumentInfo> createFromMethod(Method method, Object[] args);
}
