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
        //O(N*M)~O(N^2)
        methodRules.forEach(mr -> {
            if (!mr.getInclude().isEmpty()) {
                for (String ic : mr.getInclude()) {
                    ValidationRule br = beanRuleMap.get(mr.getTarget() + ic);
                    if (br != null) {
                        BeanUtils.copyProperties(mr, br, "include", "exclude", "target");
                    } else {
                        br = new ValidationRule();
                        br.addInclude(ic);
                        BeanUtils.copyProperties(mr, br, "include", "exclude");
                        mixedRules.add(br);
                    }
                }
            } else {
                boolean isMatch = false;
                for (ValidationRule mixedRule : mixedRules) {
                    if (mixedRule.getTarget().equals(mr.getTarget())) {
                        isMatch = true;
                        BeanUtils.copyProperties(mr, mixedRule, "include", "exclude", "target");
                    }
                }
                if (!isMatch) {
                    mixedRules.add(mr);
                }
            }
        });
        return mixedRules;
    }
}
