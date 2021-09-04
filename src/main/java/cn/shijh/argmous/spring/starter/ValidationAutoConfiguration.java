package cn.shijh.argmous.spring.starter;

import cn.shijh.argmous.manager.validation.ArrayValidationManager;
import cn.shijh.argmous.manager.validation.ValidationManager;
import cn.shijh.argmous.manager.validation.impl.DefaultValidationManager;
import cn.shijh.argmous.manager.validator.ValidatorManager;
import cn.shijh.argmous.manager.validator.impl.DefaultValidatorManager;
import cn.shijh.argmous.validator.RuleValidator;
import cn.shijh.argmous.validator.impl.RegexpValidator;
import cn.shijh.argmous.validator.impl.RequiredValidator;
import cn.shijh.argmous.validator.impl.SizeValidator;
import cn.shijh.argmous.validator.impl.ValueRangeValidator;
import javafx.scene.shape.VLineTo;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Configuration(proxyBeanMethods = false)
public class ValidationAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(ValidationManager.class)
    public ValidationManager validationManager(ObjectProvider<ValidatorManager> validatorManager) {
        return new DefaultValidationManager(validatorManager.getIfAvailable());
    }

    @Bean
    @ConditionalOnMissingBean(ArrayValidationManager.class)
    public ArrayValidationManager arrayValidationManager(ObjectProvider<ValidatorManager> validatorManager) {
        return new DefaultValidationManager(validatorManager.getIfAvailable());
    }

    @Bean
    @ConditionalOnMissingBean(ValidatorManager.class)
    public ValidatorManager validatorManager(ObjectProvider<Collection<RuleValidator>> validators) {
        List<RuleValidator> ruleValidators = defaultValidator();
        validators.ifAvailable(ruleValidators::addAll);
        return new DefaultValidatorManager(ruleValidators);
    }

    public List<RuleValidator> defaultValidator() {
        return new ArrayList<>(
                Arrays.asList(
                        new RequiredValidator(),
                        new RegexpValidator(),
                        new SizeValidator(),
                        new ValueRangeValidator()
                )
        );
    }
}
