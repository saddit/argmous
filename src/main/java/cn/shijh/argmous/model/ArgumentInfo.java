package cn.shijh.argmous.model;

import lombok.Data;

import java.lang.reflect.Parameter;

@Data
public class ArgumentInfo {
    private Object value;
    private Parameter parameter;
    private Class<?> type;
    private String name;
}
