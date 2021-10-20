package top.pressed.argmous.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class ArgumentInfo implements Serializable, Cloneable {
    private Object value;
    private Class<?> type;
    private String name;
    private String belongTo;

    @Override
    public ArgumentInfo clone() throws CloneNotSupportedException {
        return (ArgumentInfo) super.clone();
    }
}
