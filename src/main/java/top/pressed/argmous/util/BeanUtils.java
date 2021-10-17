package top.pressed.argmous.util;

import com.esotericsoftware.reflectasm.FieldAccess;
import com.esotericsoftware.reflectasm.MethodAccess;
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

    /**
     * exclude toString, equals, hashCode ....
     *
     */
    public boolean isBeanBaseMethod(String methodName) {
        return Arrays.asList("toString", "equals", "hashCode").contains(methodName);
    }

    public String getterName(String fieldName) {
        String fst = fieldName.substring(0, 1);
        return "get" + fst.toUpperCase() + fieldName.substring(1);
    }

    public String setterName(String fieldName) {
        String fst = fieldName.substring(0, 1);
        return "set" + fst.toUpperCase() + fieldName.substring(1);
    }

    public void copyProperties(Object from, Object to, String... exclude) {
        MethodAccess fromMethod = MethodAccess.get(from.getClass());
        MethodAccess toMethod = MethodAccess.get(to.getClass());

        List<String> excludeList = Arrays.stream(exclude).collect(Collectors.toList());

        for (int i = 0; i< fromMethod.getMethodNames().length; i++) {
            String toMethodName = fromMethod.getMethodNames()[i];

            if (BeanUtils.isBeanBaseMethod(toMethodName) || !toMethodName.matches("^get\\w+$")) {
                continue;
            }

            String fromFieldName = toMethodName.substring(3).toLowerCase();
            Class<?> fromFieldType = fromMethod.getReturnTypes()[i];

            try {
                int toSetterIdx = toMethod.getIndex(setterName(fromFieldName));
                int toGetterIdx = toMethod.getIndex(getterName(fromFieldName));
                Class<?> toFieldType = toMethod.getReturnTypes()[toGetterIdx];

                if (excludeList.contains(fromFieldName)) {
                    continue;
                }

                Object fromValue = fromMethod.invoke(from, getterName(fromFieldName));
                if (fromFieldType.isArray()) {
                    AnnotationBeanUtils.arrayResolve(fromValue, fromFieldType, to, toMethod, toSetterIdx, toGetterIdx);
                } else if (toFieldType.isAssignableFrom(fromFieldType)
                        || (fromFieldType.isPrimitive()
                        && ClassUtils.primitiveToWrapper(fromFieldType).equals(toFieldType))) {
                    toMethod.invoke(to, toSetterIdx, fromValue);
                }
            } catch (IllegalArgumentException ignore) {}
        }
    }
}
