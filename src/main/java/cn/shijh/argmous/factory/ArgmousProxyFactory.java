package cn.shijh.argmous.factory;

import cn.shijh.argmous.builder.ArgmousProxyFactoryBuilder;

public interface ArgmousProxyFactory {
    Object proxy(Object targetType);

    /**
     * required empty constructor
     * @param instanceClass the non abstract class which implements some interfaces
     * @return the instance of proxy
     */
    Object newProxyInstance(Class<?> instanceClass);

    static ArgmousProxyFactoryBuilder builder() {
        return new ArgmousProxyFactoryBuilder();
    }
}
