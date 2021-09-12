package cn.shijh.argmous.factory.arg;

import cn.shijh.argmous.annotation.NotValid;
import cn.shijh.argmous.annotation.Valid;
import cn.shijh.argmous.factory.ArgumentInfoFactory;
import cn.shijh.argmous.model.ArgumentInfo;
import cn.shijh.argmous.util.BeanUtils;
import lombok.val;
import org.apache.commons.lang3.ClassUtils;

import java.io.File;
import java.lang.annotation.Annotation;
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
        if(validName != null) {
            info.setName(validName.value());
        } else {
            info.setName(parameter.getName());
        }
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
            } catch (IllegalAccessException ignore) { }
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
}
