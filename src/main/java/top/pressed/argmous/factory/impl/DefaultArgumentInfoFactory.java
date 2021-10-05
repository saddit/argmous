package top.pressed.argmous.factory.impl;

import top.pressed.argmous.annotation.NotValid;
import top.pressed.argmous.annotation.Valid;
import top.pressed.argmous.exception.ArgumentCreateException;
import top.pressed.argmous.factory.ArgumentInfoFactory;
import top.pressed.argmous.model.ArgumentInfo;
import top.pressed.argmous.util.BeanUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;

public class DefaultArgumentInfoFactory implements ArgumentInfoFactory {

    @Override
    public ArgumentInfo createFromArg(Object arg, Parameter parameter) {
        ArgumentInfo info = new ArgumentInfo();
        info.setValue(arg);
        info.setType(parameter.getType());
        Valid validName = parameter.getAnnotation(Valid.class);
        if (validName != null) {
            info.setName(validName.value());
        } else {
            info.setName(parameter.getName());
        }
        info.setBelongTo(info.getName());
        return info;
    }

    @Override
    public Collection<ArgumentInfo> createFromFields(Object arg, String name, Class<?> argType) {
        Field[] declaredFields = argType.getDeclaredFields();
        Collection<ArgumentInfo> fieldInfo = new LinkedList<>();
        for (Field field : declaredFields) {
            ArgumentInfo i = new ArgumentInfo();
            i.setName(field.getName());
            i.setBelongTo(name);
            i.setType(field.getType());
            try {
                if (arg != null) {
                    field.setAccessible(true);
                    i.setValue(field.get(arg));
                } else {
                    i.setValue(null);
                }
                fieldInfo.add(i);
            } catch (IllegalAccessException ignore) {
            }
        }
        return fieldInfo;
    }

    @Override
    public Collection<ArgumentInfo> createFromMethod(Method method, Object[] args) {
        Collection<ArgumentInfo> argumentInfos = new LinkedList<>();
        Parameter[] parameters = method.getParameters();
        for (int i = 0; i < args.length; i++) {
            Parameter parameter = parameters[i];
            if (parameter.getAnnotation(NotValid.class) != null) {
                continue;
            }
            Object arg = args[i];
            ArgumentInfo fromArg = createFromArg(arg, parameter);
            argumentInfos.add(fromArg);
            if (BeanUtils.isBean(parameter.getType())) {
                Collection<ArgumentInfo> fromFields = createFromFields(arg, fromArg.getName(), parameter.getType());
                argumentInfos.addAll(fromFields);
            }
        }
        return argumentInfos;
    }

    @Override
    public Collection<ArgumentInfo> createFromArray(Collection<?> objects, String name) throws ArgumentCreateException {
        //对数组的每个参数进行检查则必须考虑数组内为对象时对对象属性进行拆分
        Collection<ArgumentInfo> argumentInfos = new LinkedList<>();
        if (objects != null) {
            int count = 0;
            for (Object o : objects) {
                if (BeanUtils.isBean(o.getClass())) {
                    argumentInfos.addAll(createFromFields(o, name, o.getClass()));
                } else {
                    ArgumentInfo arg = new ArgumentInfo();
                    arg.setType(o.getClass());
                    arg.setValue(o);
                    arg.setName(name + "[" + (count++) + "]");
                    arg.setBelongTo(name);
                    argumentInfos.add(arg);
                }
            }
        }
        return argumentInfos;
    }
}
