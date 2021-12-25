package top.pressed.argmous.annotation.factory;

import top.pressed.argmous.factory.ValidationRuleFactory;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Inherited
@Retention(RetentionPolicy.RUNTIME)
public @interface OverrideTo {
    Class<? extends ValidationRuleFactory> value();
}
