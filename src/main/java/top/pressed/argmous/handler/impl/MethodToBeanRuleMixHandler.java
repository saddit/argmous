package top.pressed.argmous.handler.impl;

import org.apache.commons.lang3.SerializationUtils;
import top.pressed.argmous.StandardInitBean;
import top.pressed.argmous.handler.RuleMixHandler;
import top.pressed.argmous.model.ValidationRule;
import top.pressed.argmous.util.BeanUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

public class MethodToBeanRuleMixHandler implements RuleMixHandler, StandardInitBean {
    @Override
    public Collection<ValidationRule> mix(Collection<ValidationRule> beanRules, Collection<ValidationRule> methodRules) {
        ArrayList<ValidationRule> mixedRules = SerializationUtils.clone(new ArrayList<>(beanRules));
        //O(N)
        Map<String, ValidationRule> beanRuleMap = mixedRules.stream()
                .collect(Collectors.toMap(i -> i.getTarget() + "-" + i.getFirstInclude(), i -> i));
        //O(N*M)~O(N^2)
        methodRules.forEach(mr -> {
            if (!mr.getInclude().isEmpty()) {
                for (String ic : mr.getInclude()) {
                    ValidationRule br = beanRuleMap.get(mr.getTarget() + "-" + ic);
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
