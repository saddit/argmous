package cn.shijh.argmous.constrant;

public class DefaultRegexp {
    //language=regexp
    public static final String EMAIL = "^[\\w\\d-_]+@[\\w\\d]+.[\\w]+$";
    //language=regexp
    public static final String PHONE_AREA = "^\\+?[\\d]{13}$";
    //language=regexp
    public static final String PHONE = "^\\d{11}$";
}
