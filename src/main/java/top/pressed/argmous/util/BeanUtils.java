package top.pressed.argmous.util;

import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.ClassUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class BeanUtils {
    public boolean isBean(Class<?> o) {
        return !ClassUtils.isPrimitiveOrWrapper(o) &&
                !String.class.isAssignableFrom(o) &&
                !Iterable.class.isAssignableFrom(o) &&
                !Member.class.isAssignableFrom(o);
    }

    public void copyProperties(Object from, Object to, String... exclude) {
        Field[] fields = from.getClass().getDeclaredFields();
        Class<?> toClass = to.getClass();
        List<String> excludeList = Arrays.stream(exclude).collect(Collectors.toList());
        for (Field field : fields) {
            if (excludeList.contains(field.getName())) {
                continue;
            }
            try {
                field.setAccessible(true);
                Object fromValue = field.get(from);
                Field toField = toClass.getDeclaredField(field.getName());
                toField.setAccessible(true);
                if (fromValue.getClass().isArray()) {
                    AnnotationBeanUtils.arrayResolve(fromValue, toField, to);
                } else if (toField.getType().isAssignableFrom(field.getType())){
                    toField.set(to, fromValue);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
