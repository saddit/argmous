package top.pressed.argmous.model;

import lombok.Data;

import java.lang.reflect.Parameter;

@Data
public class ArgumentInfo {
    private Object value;
    private Class<?> type;
    private String name;
    private String belongTo;
}
