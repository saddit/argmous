package cn.shijh.argmous.context;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 'exclude'、'include'只能使用其中一个,如果两个都用，那么只使用’include‘ <br/>
 * @author shijh
 */
@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ParamCheck {
    String regexp() default "";
    String split() default ",";
    /**
     * 前闭后开 -1 为不限制 只填一个相当于 [n,+∞）
     */
    int[] size() default {};
    /**
     * 前闭后开 空字符 为不限制 只填一个相当于 [n,+∞)
     */
    String[] range() default {};
    boolean split2Array() default false;
    boolean required() default true;
    String target() default "";
    String[] include() default {};
    String[] exclude() default {};
    String[] custom() default {};
    //region deprecated
    @Deprecated
    int lengthLT() default -1;
    @Deprecated
    int lengthLE() default -1;
    @Deprecated
    int lengthEQ() default -1;
    @Deprecated
    int lengthGT() default -1;
    @Deprecated
    int lengthGE() default -1;
    //endregion
}
