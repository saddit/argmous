package cn.shijh.argmous.util;

import cn.shijh.argmous.model.ArgumentInfo;
import org.apache.commons.lang3.ClassUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Field;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

public class ArgumentUtils {

    private static Collection<ArgumentInfo> wrapperFields(Object o, Class<?> clazz, String target) {
        List<ArgumentInfo> argInfos = new ArrayList<>(3);
        for (Field field : clazz.getDeclaredFields()) {
            ArgumentInfo argInfo = new ArgumentInfo();
            argInfo.setType(field.getType());
            argInfo.setName(field.getName());
            argInfo.setParentName(target);
            if (o != null) {
                field.setAccessible(true);
                try {
                    argInfo.setValue(field.get(o));
                } catch (IllegalAccessException ignore) {}
            }
            argInfos.add(argInfo);
        }
        return argInfos;
    }

    public static List<ArgumentInfo> wrapper(JoinPoint jp, Function<Class<?>, Boolean> exclude) {
        Object[] args = jp.getArgs();
        MethodSignature signature = (MethodSignature) jp.getSignature();
        Class<?>[] argTypes = signature.getParameterTypes();
        String[] argNames = signature.getParameterNames();
        List<ArgumentInfo> argumentInfos = new ArrayList<>(args.length);
        for (int i = 0; i < args.length; i++) {
            Class<?> argType = argTypes[i];
            Object arg = args[i];
            ArgumentInfo argumentInfo = new ArgumentInfo();
            if (ClassUtils.isPrimitiveOrWrapper(argType)) {
                argumentInfo.setValue(arg);
                argumentInfo.setName(argNames[i]);
                argumentInfo.setType(argType);
                argumentInfo.setParentName(argNames[i]);
                argumentInfos.add(argumentInfo);
            } else if (exclude == null || !exclude.apply(argType)) {
                argumentInfos.addAll(wrapperFields(arg,argType, argNames[i]));
            }
        }
        return argumentInfos;
    }
}
