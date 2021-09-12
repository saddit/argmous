package cn.shijh.argmous.factory;

import cn.shijh.argmous.annotation.ParamCheck;
import cn.shijh.argmous.model.ValidationRule;

import java.util.Collection;

public interface ValidationRuleFactory {
    Collection<ValidationRule> createFromAnnotations(ParamCheck[] paramChecks);
    ValidationRule createFromAnnotation(ParamCheck paramCheck);
}
