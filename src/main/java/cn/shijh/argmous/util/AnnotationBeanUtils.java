package cn.shijh.argmous.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;

public class AnnotationBeanUtils {

    private static void arrayResolve(Object[] array, Field beanFiled, Object bean) throws IllegalAccessException{
        if (Collection.class.isAssignableFrom(beanFiled.getType())) {
            beanFiled.set(bean, Arrays.asList(array));
        } else if (beanFiled.getType().isArray()) {
            beanFiled.set(bean, array);
        }
    }

    public static void copyProperties(Annotation annotation, Object bean) {
        Class<? extends Annotation> annotationType = annotation.annotationType();
        Class<?> beanType = bean.getClass();
        Field[] annotationFields = annotationType.getDeclaredFields();
        for (Field field : annotationFields) {
            field.setAccessible(true);
            try {
                Field beanField = beanType.getDeclaredField(field.getName());
                beanField.setAccessible(true);
                Object annotationValue = field.get(annotation);
                if (field.getType().isArray()) {
                    Object[] array = (Object[]) annotationValue;
                    arrayResolve(array, beanField, bean);
                } else {
                    beanField.set(bean, annotationValue);
                }
            } catch (NoSuchFieldException | IllegalAccessException ignore) { }
        }
    }
}
