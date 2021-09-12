package cn.shijh.argmous.util;

import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.ClassUtils;
import java.lang.reflect.Member;

@UtilityClass
public class BeanUtils {
    public boolean isBean(Class<?> o) {
        return !ClassUtils.isPrimitiveOrWrapper(o) &&
                !String.class.isAssignableFrom(o) &&
                !Iterable.class.isAssignableFrom(o) &&
                !Member.class.isAssignableFrom(o);
    }
}
