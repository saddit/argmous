package cn.shijh.argmous.validator.impl;


import cn.shijh.argmous.model.ValidationRule;
import cn.shijh.argmous.validator.RuleValidator;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.Arrays;
import java.util.List;


public class ValueRangeValidator implements RuleValidator {

    @Override
    public boolean support(Class<?> paramType, ValidationRule rule) {
        return Number.class.isAssignableFrom(paramType) && rule.getRange().size() > 0;
    }

    @Override
    public String errorMessage(ValidationRule rule) {
        return "number should be included in [" + StringUtils.join(rule.getRange(),",") + ")";
    }

    @Override
    public boolean validate(Object object, ValidationRule rule) throws IllegalStateException {
        numberCheck(object);
        String[] range = rule.getRange().toArray(new String[0]);
        boolean isInt = object instanceof Integer;
        boolean valid = true;
        if (range.length > 0 && !range[0].isEmpty()) {
            numberCheck(range[0]);
            valid = valueCheck(object.toString(),range[0], isInt,1, 0);
        }
        if (range.length > 1 && !range[1].isEmpty()) {
            numberCheck(range[1]);
            valid = valid && valueCheck(object.toString(),range[1], isInt,-1);
        }
        return valid;
    }

    protected void numberCheck(Object object) {
        if (!(object instanceof Number)) {
            throw new IllegalArgumentException(object.getClass().getSimpleName() + " not support to check value range");
        }
    }

    protected void numberCheck(String s) {
        if (!NumberUtils.isCreatable(s)) {
            throw new IllegalArgumentException(s + " is not a number");
        }
    }

    protected boolean valueCheck(String v1, String v2, boolean isInt, Integer... rule) {
        List<Integer> rules = Arrays.asList(rule);
        try {
            if (isInt) {
                return rules.contains(intCompare(NumberUtils.createInteger(v1), NumberUtils.createInteger(v2)));
            } else {
                return rules.contains(doubleCompare(NumberUtils.createDouble(v1), NumberUtils.createDouble(v2)));
            }
        } catch (NumberFormatException e) {
            throw new IllegalStateException(e);
        }
    }

    private Integer intCompare(Integer v1, Integer v2) {
        return v1.compareTo(v2);
    }

    private Integer doubleCompare(Double v1, Double v2) {
        return v1.compareTo(v2);
    }
}
