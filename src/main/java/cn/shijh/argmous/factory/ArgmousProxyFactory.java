package cn.shijh.argmous.factory;

import cn.shijh.argmous.builder.ArgmousProxyFactoryBuilder;

public interface ArgmousProxyFactory {
    Object proxy(Object targetType);

    static ArgmousProxyFactoryBuilder builder() {
        return new ArgmousProxyFactoryBuilder();
    }
}
