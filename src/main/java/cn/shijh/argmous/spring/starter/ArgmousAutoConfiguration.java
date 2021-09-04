package cn.shijh.argmous.spring.starter;

import cn.shijh.argmous.manager.validation.ArrayValidationManager;
import cn.shijh.argmous.manager.validation.ValidationManager;
import cn.shijh.argmous.manager.validator.ValidatorManager;
import cn.shijh.argmous.spring.context.ParamCheckAdvice;
import cn.shijh.argmous.spring.properties.ArgmousProperties;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(
        prefix = "spring.argmous", name = "enable",
        havingValue = "true",
        matchIfMissing = true
)
@EnableConfigurationProperties(ArgmousProperties.class)
@AutoConfigureAfter(ValidationAutoConfiguration.class)
public class ArgmousAutoConfiguration {
    private final ApplicationContext applicationContext;
    private final ArgmousProperties properties;
    private final ValidationManager validationManager;
    private final ArrayValidationManager arrayValidationManager;

    public ArgmousAutoConfiguration(ApplicationContext applicationContext, ArgmousProperties properties, ObjectProvider<ValidationManager> validationManager, ObjectProvider<ArrayValidationManager> arrayValidationManager) {
        this.applicationContext = applicationContext;
        this.properties = properties;
        this.validationManager = validationManager.getIfAvailable();
        this.arrayValidationManager = arrayValidationManager.getIfAvailable();
    }

    @Bean
    public ParamCheckAdvice paramCheckAdvice() {
        ParamCheckAdvice advice = new ParamCheckAdvice();
        advice.setApplicationContext(applicationContext);
        advice.setArrayValidationManager(arrayValidationManager);
        advice.setValidationManager(validationManager);
        if (properties.getOrder() != null) {
            advice.setOrder(properties.getOrder());
        }
        return advice;
    }
}
