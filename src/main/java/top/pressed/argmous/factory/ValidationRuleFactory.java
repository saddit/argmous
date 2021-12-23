package top.pressed.argmous.factory;

import lombok.NonNull;
import top.pressed.argmous.exception.RuleCreateException;
import top.pressed.argmous.model.ValidationRule;

import java.lang.reflect.Method;
import java.util.Collection;

public interface ValidationRuleFactory {
    /**
     * @param method      method
     * @param argNames    argument names
     * @param ignoreArray array supported
     * @return rules
     * @throws RuleCreateException if something wrong
     */
    Collection<ValidationRule> create(@NonNull Method method, Object[] values, @NonNull String[] argNames, boolean ignoreArray) throws RuleCreateException;
}
