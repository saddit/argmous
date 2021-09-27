package cn.shijh.argmous.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ArrayParamCheck {
    String id() default "";
    ParamCheck[] value() default {};
    ParamCheck self() default @ParamCheck(size = {1,-1});
    String target();
}
