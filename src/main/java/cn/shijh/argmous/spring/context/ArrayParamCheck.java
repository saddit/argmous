package cn.shijh.argmous.spring.context;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ArrayParamCheck {
    ParamCheck[] value() default {};
    boolean required() default true;
    String target() default "";
}
