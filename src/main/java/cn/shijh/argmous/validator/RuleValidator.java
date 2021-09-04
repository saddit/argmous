package cn.shijh.argmous.validator;


import cn.shijh.argmous.model.ValidationRule;

public interface RuleValidator {
    /**
     * validate argument
     * @param object argument
     * @param rule rule
     * @return true if passed
     * @throws IllegalStateException if something got wrong
     */
    boolean validate(Object object, ValidationRule rule) throws IllegalStateException;

    /**
     * return the notify message of an no passed validating
     * @param rule rule
     * @return notify message
     */
    String errorMessage(ValidationRule rule);

    /**
     * does support to be checked ?
     * @param paramType argument's type
     * @param rule rule
     * @return true if supported
     */
    boolean support(Class<?> paramType, ValidationRule rule);
}
