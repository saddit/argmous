package top.pressed.argmous.handler.impl;

import org.apache.commons.lang3.SerializationUtils;
import top.pressed.argmous.StandardInstanceBean;
import top.pressed.argmous.annotation.factory.OverrideTo;
import top.pressed.argmous.factory.ValidationRuleFactory;
import top.pressed.argmous.handler.RuleMixHandler;
import top.pressed.argmous.handler.SpecTopologyGraph;
import top.pressed.argmous.model.ValidationRule;
import top.pressed.argmous.util.BeanUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class TopologyMixingHandler implements RuleMixHandler, StandardInstanceBean {

    @Deprecated
    public Collection<ValidationRule> mix(Collection<ValidationRule> overridden, Collection<ValidationRule> override) {
        ArrayList<ValidationRule> mixedRules = SerializationUtils.clone(new ArrayList<>(overridden));
        //O(N)
        Map<String, ValidationRule> overriddenRuleMap = mixedRules.stream()
                .collect(Collectors.toMap(i -> i.getTarget() + "-" + i.getFirstInclude(), i -> i));
        //O(N*M)~O(N^2)
        override.forEach(mr -> {
            if (!mr.getInclude().isEmpty()) {
                for (String ic : mr.getInclude()) {
                    ValidationRule br = overriddenRuleMap.get(mr.getTarget() + "-" + ic);
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

    private SpecTopologyGraph<Class<? extends ValidationRuleFactory>> initTopologyGraph(Set<Class<? extends ValidationRuleFactory>> keySet) {
        SpecTopologyGraph<Class<? extends ValidationRuleFactory>> graph = new SpecTopologyGraph<>();
        for (Class<? extends ValidationRuleFactory> startClass : keySet) {
            OverrideTo annotation = startClass.getAnnotation(OverrideTo.class);
            if (annotation != null) {
                for (Class<? extends ValidationRuleFactory> endClass : keySet) {
                    if (annotation.value().isAssignableFrom(endClass)) {
                        graph.add(startClass, endClass);
                    }
                }
            }
        }
        return graph;
    }

    @Override
    public Collection<ValidationRule> handle(Map<Class<? extends ValidationRuleFactory>, Collection<ValidationRule>> dataMap) {
        SpecTopologyGraph<Class<? extends ValidationRuleFactory>> graph = initTopologyGraph(dataMap.keySet());
        return dataMap.get(graph.popEach((s, e) -> {
            if (e != null) {
                Collection<ValidationRule> sr = dataMap.get(s);
                Collection<ValidationRule> er = dataMap.get(e);
                dataMap.put(e, mix(er, sr));
            }
        }));
    }
}
