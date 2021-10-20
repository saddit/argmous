package top.pressed.argmous.factory;


public interface ArgmousProxyFactory {
    Object proxy(Object targetType);

    /**
     * required empty constructor
     * @param instanceClass the non abstract class which implements some interfaces
     * @return the instance of proxy
     */
    Object newProxyInstance(Class<?> instanceClass);
}
