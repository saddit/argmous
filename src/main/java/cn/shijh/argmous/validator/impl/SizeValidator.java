package cn.shijh.argmous.validator.impl;


import cn.shijh.argmous.context.ParamCheck;
import cn.shijh.argmous.validator.RuleValidator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class SizeValidator implements RuleValidator {
    @Override
    public boolean support(Class<?> paramType, ParamCheck rule) {
        return rule.range().length > 0
                && (Collection.class.isAssignableFrom(paramType)
                || rule.split2Array()
                || String.class.isAssignableFrom(paramType));
    }

    @Override
    public String errorMessage(ParamCheck rule) {
        int[] size = rule.size();
        String msg = Arrays.stream(size).mapToObj(i -> {
            if (i == -1) {
                return "";
            }
            return String.valueOf(i);
        }).collect(Collectors.joining(","));
        return "length should be included in [" + msg + ")";
    }

    @Override
    public boolean validate(Object param, ParamCheck anno) {
        boolean length = true;
        int[] range = anno.size();
        if (range.length > 0 && range[0] != -1) {
            length = lengthCheck(anno, param, range[0], -1, 0);
        }
        if (range.length > 1 && range[1] != -1) {
            length = length && lengthCheck(anno, param, range[0], 1);
        }
        return length;
    }

    /**
     * rule 最多三种 -1 0 1
     */
    private boolean lengthCheck(ParamCheck anno, Object o, Integer val, Integer... rule) {
        List<Integer> rules = Arrays.asList(rule);
        int l = 0;
        if (o instanceof Collection) {
            l = ((Collection) o).size();
        } else if (o.getClass().isArray()) {
            l = Array.getLength(o);
        } else if (anno.split2Array()) {
            String[] array = o.toString().split(anno.split());
            l = array.length;
        } else if (o instanceof String) {
            l = ((String) o).length();
        }
        return (l == 0 && !anno.required()) || rules.contains(val.compareTo(l));
    }
}
