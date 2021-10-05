package top.pressed.argmous.annotation.bean;

import top.pressed.argmous.annotation.IsRule;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@IsRule("split")
public @interface Split {
    String value();
}
