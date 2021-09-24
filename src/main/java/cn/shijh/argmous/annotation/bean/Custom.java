package cn.shijh.argmous.annotation.bean;

import cn.shijh.argmous.annotation.IsRule;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@IsRule("custom")
public @interface Custom {
    String value();
}
