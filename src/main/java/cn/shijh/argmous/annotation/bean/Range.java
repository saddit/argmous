package cn.shijh.argmous.annotation.bean;

import cn.shijh.argmous.annotation.IsRule;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@IsRule("range")
public @interface Range {
    String[] value();
}
