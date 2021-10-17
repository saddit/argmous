package top.pressed.argmous.util;

import com.esotericsoftware.reflectasm.ConstructorAccess;
import com.esotericsoftware.reflectasm.FieldAccess;
import com.esotericsoftware.reflectasm.MethodAccess;
import org.apache.commons.lang3.ClassUtils;

import javax.management.ReflectionException;
import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

public class AnnotationBeanUtils {

//    public static void arrayResolve(Object array, Field beanFiled, Object bean) throws IllegalAccessException, NoSuchMethodException, InstantiationException, InvocationTargetException {
//        Class<?> arrayType = array.getClass();
//        Class<?> componentType = arrayType.getComponentType();
//        Object[] curArray;
//        if (componentType.isPrimitive()) {
//            curArray = toWrapperArray(array,componentType);
//        } else {
//            curArray = (Object[]) array;
//        }
//        if (Collection.class.isAssignableFrom(beanFiled.getType())) {
//            beanFiled.set(bean, new ArrayList<>(Arrays.asList(curArray)));
//        } else if (beanFiled.getType().isArray()) {
//            beanFiled.set(bean, array);
//        }
//    }

    /**
     * set 'array' to field of bean by using MethodAccess. Need provide setter index and getter index of bean's field
     *
     * @param array value to set (nullable)
     * @param arrayType the class of value
     * @param bean bean to be changed
     * @param beanMethodAccess bean method access
     * @param setterIdx field setter method index
     * @param getterIdx field getter method index
     */
    public static void arrayResolve(Object array, Class<?> arrayType,
                                    Object bean, MethodAccess beanMethodAccess, int setterIdx, int getterIdx) {
        Class<?> componentType = arrayType.getComponentType();
        Object[] curArray;
        if (componentType.isPrimitive()) {
            curArray = toWrapperArray(array,componentType);
        } else {
            curArray = array != null ? (Object[]) array : new Object[0];
        }
        Class<?> beanFieldType = beanMethodAccess.getReturnTypes()[getterIdx];
        if (Collection.class.isAssignableFrom(beanFieldType)) {
            beanMethodAccess.invoke(bean, setterIdx, new ArrayList<>(Arrays.asList(curArray)));
        } else if (beanFieldType.isArray()) {
            beanMethodAccess.invoke(bean, setterIdx, array);
        }
    }

    private static Object[] toWrapperArray(Object array, Class<?> compType) {
        int l = array != null ? Array.getLength(array) : 0;
        Class<?> wrapperType = ClassUtils.primitiveToWrapper(compType);
        Object wrapperArray = Array.newInstance(wrapperType, l);
        try {
            Constructor<?> constructor = wrapperType.getConstructor(compType);
            if (array != null) {
                for (int i = 0; i < l; i++) {
                    Object elem = Array.get(array, i);
                    Object wrapperElem = null;
                    wrapperElem = constructor.newInstance(elem);
                    Array.set(wrapperArray, i, wrapperElem);
                }
            }
        }  catch (InstantiationException | IllegalAccessException |
                InvocationTargetException | NoSuchMethodException ignore) {
            throw new IllegalArgumentException("[toWrapperArray] compType is not primitive");
        }
        return (Object[]) wrapperArray;
    }

    public static void copyProperties(Annotation annotation, Object bean) {
        if (bean == null) {
            throw new IllegalArgumentException("copyProperties: bean can not be null");
        }
        Class<? extends Annotation> annotationType = annotation.annotationType();
        Class<?> beanType = bean.getClass();
        MethodAccess beanMethodAccess = MethodAccess.get(beanType);
        MethodAccess annoMethodAccess = MethodAccess.get(annotationType);
        for (int i = 0; i < annoMethodAccess.getMethodNames().length; i++) {
            String annotationMethodName = annoMethodAccess.getMethodNames()[i];
            if (BeanUtils.isBeanBaseMethod(annotationMethodName)) {
                continue;
            }
            Object annotationValue = annoMethodAccess.invoke(annotation, i);
            Class<?> annotationValueType = annoMethodAccess.getReturnTypes()[i];

            try {
                int beanSetterIdx = beanMethodAccess.getIndex(BeanUtils.setterName(annotationMethodName));
                int beanGetterIdx = beanMethodAccess.getIndex(BeanUtils.getterName(annotationMethodName));

                Class<?> beanFieldType = beanMethodAccess.getReturnTypes()[beanGetterIdx];

                if (annoMethodAccess.getReturnTypes()[i].isArray()) {
                    arrayResolve(annotationValue, annotationValueType, bean, beanMethodAccess, beanSetterIdx, beanGetterIdx);
                } else if (beanFieldType.isAssignableFrom(annotationValueType)
                        || (annotationValueType.isPrimitive()
                        && ClassUtils.primitiveToWrapper(annotationValueType).equals(beanFieldType))) {
                    beanMethodAccess.invoke(bean, beanSetterIdx, annotationValue);
                }
            } catch (IllegalArgumentException ignore) {};
        }
    }

    /**
     * mapping annotation value to bean
     * Map.Key is annotation method name
     * Map.Value is bean field name
     */
    public static void copyProperties(Annotation annotation, Object bean, Map<String, String> mapping) {
        if (bean == null) {
            throw new IllegalArgumentException("copyProperties: bean can not be null");
        }
        MethodAccess annoMethodAccess = MethodAccess.get(annotation.getClass());
        MethodAccess beanMethodAccess = MethodAccess.get(bean.getClass());
        mapping.forEach((k,v)->{
            //annotation getter's name isn't like 'getSomething'
            int annoGetterIdx = annoMethodAccess.getIndex(k);
            Class<?> annoFieldType = annoMethodAccess.getReturnTypes()[annoGetterIdx];

            int beanSetterIdx = beanMethodAccess.getIndex(BeanUtils.setterName(v));
            int beanGetterIdx = beanMethodAccess.getIndex(BeanUtils.getterName(v));

            Object annoValue = annoMethodAccess.invoke(annotation, annoGetterIdx);
            if (annoFieldType.isArray()) {
                AnnotationBeanUtils.arrayResolve(annoValue, annoFieldType, bean, beanMethodAccess, beanSetterIdx, beanGetterIdx);
            } else {
                beanMethodAccess.invoke(bean, beanSetterIdx, annoValue);
            }
        });
    }
}
