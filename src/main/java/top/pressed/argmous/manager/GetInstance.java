package top.pressed.argmous.manager;

import java.rmi.NoSuchObjectException;

public interface GetInstance {
    <T> T getInstance(Class<T> type) throws NoSuchObjectException;
}
