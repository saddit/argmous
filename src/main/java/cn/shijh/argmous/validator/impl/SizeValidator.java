package cn.shijh.argmous.validator.impl;


import cn.shijh.argmous.model.ValidationRule;
import cn.shijh.argmous.validator.RuleValidator;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class SizeValidator implements RuleValidator {
    @Override
    public boolean support(Class<?> paramType, ValidationRule rule) {
        return rule.getRange().size() > 0
                && (Collection.class.isAssignableFrom(paramType)
                || rule.getSplit2Array()
                || String.class.isAssignableFrom(paramType));
    }

    @Override
    public String errorMessage(ValidationRule rule) {
        Collection<Integer> size = rule.getSize();
        String msg = size.stream().map(i -> {
            if (i == -1) {
                return "";
            }
            return String.valueOf(i);
        }).collect(Collectors.joining(","));
        return "length should be included in [" + msg + ")";
    }

    @Override
    public boolean validate(Object param, ValidationRule rule) {
        boolean length = true;
        Integer[] size = rule.getSize().toArray(new Integer[0]);
        if (size.length > 0 && size[0] != -1) {
            length = lengthCheck(rule, param, size[0], -1, 0);
        }
        if (size.length > 1 && size[1] != -1) {
            length = length && lengthCheck(rule, param, size[0], 1);
        }
        return length;
    }

    /**
     * rule 最多三种 -1 0 1
     */
    private boolean lengthCheck(ValidationRule validationRule, Object o, Integer val, Integer... rule) {
        List<Integer> rules = Arrays.asList(rule);
        int l = 0;
        if (o instanceof Collection) {
            l = ((Collection<?>) o).size();
        } else if (o.getClass().isArray()) {
            l = Array.getLength(o);
        } else if (validationRule.getSplit2Array()) {
            String[] array = o.toString().split(validationRule.getSplit());
            l = array.length;
        } else if (o instanceof String) {
            l = ((String) o).length();
        }
        return (l == 0 && !validationRule.getRequired()) || rules.contains(val.compareTo(l));
    }
}
