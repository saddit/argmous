package cn.shijh.argmous.handler.impl;

import cn.shijh.argmous.handler.RuleMixHandler;
import cn.shijh.argmous.model.ValidationRule;
import cn.shijh.argmous.util.BeanUtils;

import javax.swing.text.html.Option;
import java.util.*;
import java.util.stream.Collectors;

public class MethodToBeanRuleMixHandler implements RuleMixHandler {
    @Override
    public Collection<ValidationRule> mix(Collection<ValidationRule> beanRules, Collection<ValidationRule> methodRules) {
        ArrayList<ValidationRule> mixedRules = new ArrayList<>(beanRules);
        //O(N)
        Map<String, ValidationRule> beanRuleMap = beanRules.stream()
                .collect(Collectors.toMap(i -> i.getTarget() + i.getFirstInclude(), i -> i));
        //O(N*M)
        methodRules.forEach(mr -> {
            if (mr.getInclude().isEmpty()) {
                mixedRules.add(mr);
            } else {
                mr.getInclude().forEach(ic -> {
                    ValidationRule br = beanRuleMap.get(mr.getTarget() + ic);
                    if (br == null) {
                        mixedRules.add(mr);
                    } else {
                        BeanUtils.copyProperties(mr, br, "include");
                    }
                });
            }
        });
        return mixedRules;
    }
}
