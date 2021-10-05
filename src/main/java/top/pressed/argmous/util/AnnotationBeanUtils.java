package top.pressed.argmous.util;

import org.apache.commons.lang3.ClassUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

public class AnnotationBeanUtils {

    public static void arrayResolve(Object array, Field beanFiled, Object bean) throws IllegalAccessException, NoSuchMethodException, InstantiationException, InvocationTargetException {
        Class<?> arrayType = array.getClass();
        Class<?> componentType = arrayType.getComponentType();
        Object[] curArray;
        if (componentType.isPrimitive()) {
            curArray = toWrapperArray(array,componentType);
        } else {
            curArray = (Object[]) array;
        }
        if (Collection.class.isAssignableFrom(beanFiled.getType())) {
            beanFiled.set(bean, new ArrayList<>(Arrays.asList(curArray)));
        } else if (beanFiled.getType().isArray()) {
            beanFiled.set(bean, array);
        }
    }

    private static Object[] toWrapperArray(Object array, Class<?> compType) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        int l = Array.getLength(array);
        Class<?> wrapperType = ClassUtils.primitiveToWrapper(compType);
        Object wrapperArray = Array.newInstance(wrapperType, l);
        Constructor<?> wrapperConstructor = wrapperType.getConstructor(compType);
        for (int i = 0; i < l; i++) {
            Object elem = Array.get(array, i);
            Object wrapperElem = wrapperConstructor.newInstance(elem);
            Array.set(wrapperArray, i, wrapperElem);
        }
        return (Object[]) wrapperArray;
    }

    public static void copyProperties(Annotation annotation, Object bean) {
        Class<? extends Annotation> annotationType = annotation.annotationType();
        Class<?> beanType = bean.getClass();
        Method[] methods = annotationType.getMethods();
        for (Method method : methods) {
            try {
                Field beanField = beanType.getDeclaredField(method.getName());
                beanField.setAccessible(true);
                Object annotationValue = method.invoke(annotation);
                if ( annotationValue.getClass().isArray()) {
                    arrayResolve(annotationValue, beanField, bean);
                } else if (beanField.getType().isAssignableFrom(annotationValue.getClass())){
                    beanField.set(bean, annotationValue);
                }
            } catch (NoSuchFieldException | IllegalAccessException | InvocationTargetException | NoSuchMethodException | InstantiationException ignore) { }
        }
    }

    /**
     * mapping annotation value to bean
     * Map.Key is annotation method name
     * Map.Value is bean field name
     */
    public static void copyProperties(Annotation annotation, Object bean, Map<String, String> mapping) {
        mapping.forEach((k,v)->{
            try {
                Method keyMethod = annotation.getClass().getDeclaredMethod(k);
                Field valueField = bean.getClass().getDeclaredField(v);
                valueField.setAccessible(true);
                Object key = keyMethod.invoke(annotation);
                if (key.getClass().isArray()) {
                    AnnotationBeanUtils.arrayResolve(key,valueField, bean);
                } else {
                    valueField.set(bean, key);
                }
            } catch (Exception ignored) { }
        });
    }
}
