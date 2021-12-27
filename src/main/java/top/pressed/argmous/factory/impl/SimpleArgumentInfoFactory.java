package top.pressed.argmous.factory.impl;

import com.esotericsoftware.reflectasm.MethodAccess;
import top.pressed.argmous.StandardInstanceBean;
import top.pressed.argmous.annotation.NotValid;
import top.pressed.argmous.exception.ArgumentCreateException;
import top.pressed.argmous.factory.ArgumentInfoFactory;
import top.pressed.argmous.model.ArgumentInfo;
import top.pressed.argmous.util.BeanUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Collection;
import java.util.LinkedList;

public class SimpleArgumentInfoFactory implements ArgumentInfoFactory, StandardInstanceBean {

    public ArgumentInfo createSingle(Object arg, Parameter parameter, String name) {
        ArgumentInfo info = new ArgumentInfo();
        info.setValue(arg);
        info.setType(parameter.getType());
        info.setName(name);
        info.setBelongTo(info.getName());
        return info;
    }

    public Collection<ArgumentInfo> createFromField(Object arg, String parentName, Class<?> argType) {
        MethodAccess methodAccess = MethodAccess.get(argType);
        Collection<ArgumentInfo> fieldInfo = new LinkedList<>();

        for (Field field : argType.getDeclaredFields()) {
            int getterIdx = methodAccess.getIndex(BeanUtils.getterName(field.getName()));
            ArgumentInfo i = new ArgumentInfo();
            i.setName(field.getName());
            i.setBelongTo(parentName);
            i.setType(methodAccess.getReturnTypes()[getterIdx]);
            if (arg != null) {
                i.setValue(methodAccess.invoke(arg, getterIdx));
            } else {
                i.setValue(null);
            }
            fieldInfo.add(i);
        }

        return fieldInfo;
    }

    public Collection<ArgumentInfo> createFromArray(Object[] objects, String name) throws ArgumentCreateException {
        //对数组的每个参数进行检查则必须考虑数组内为对象时对对象属性进行拆分
        Collection<ArgumentInfo> argumentInfos = new LinkedList<>();
        if (objects != null) {
            int count = 0;
            for (Object o : objects) {
                if (BeanUtils.isBean(o.getClass())) {
                    argumentInfos.addAll(createFromField(o, name, o.getClass()));
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

    @Override
    public Collection<ArgumentInfo> create(Method method, Object[] values, String[] names, boolean ignoreArray) throws ArgumentCreateException {

        Parameter[] parameters = method.getParameters();

        Collection<ArgumentInfo> argumentInfos = new LinkedList<>();

        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];
            if (parameter.getAnnotation(NotValid.class) != null) {
                continue;
            }
            Object arg = values[i];
            ArgumentInfo fromArg = createSingle(arg, parameter, names[i]);
            argumentInfos.add(fromArg);
            if (BeanUtils.isBean(parameter.getType())) {
                Collection<ArgumentInfo> fromFields = createFromField(arg, fromArg.getName(), parameter.getType());
                argumentInfos.addAll(fromFields);
            } else if (arg instanceof Collection && !ignoreArray) {
                Collection<ArgumentInfo> fromArray = createFromArray(((Collection<?>) arg).toArray(), fromArg.getName());
                argumentInfos.addAll(fromArray);
            } else if (parameter.getType().isArray() && !ignoreArray) {
                Collection<ArgumentInfo> fromArray = createFromArray(((Object[]) arg), fromArg.getName());
                argumentInfos.addAll(fromArray);
            }
        }

        return argumentInfos;
    }
}
