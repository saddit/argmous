package top.pressed.argmous.annotation.bean;

import top.pressed.argmous.annotation.IsRule;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@IsRule("custom")
public @interface Custom {
    String value();
}
