package cn.shijh.argmous.model;

import lombok.Data;
import lombok.Getter;
import lombok.ToString;

import java.util.Collection;

@Getter
@ToString
public class ValidationRule {
    private String regexp;

    private String split;

    private Boolean split2Array;

    private Collection<Integer> size;

    private Collection<String> range;

    private Boolean required;

    private String target;

    private Collection<String> include;

    private Collection<String> exclude;

    private Collection<String> custom;
}
