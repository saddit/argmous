package top.pressed.argmous.annotation.factory;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Inherited
@Retention(RetentionPolicy.RUNTIME)
public @interface InstancePriority {
    int value();
}
