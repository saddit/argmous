package cn.shijh.argmous;

import cn.shijh.argmous.factory.ValidationRuleFactory;
import cn.shijh.argmous.factory.rule.DefaultValidationRuleFactory;
import cn.shijh.argmous.model.ValidationRule;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;

@SuppressWarnings("all")
public class FactoryUnitTest {
    private ValidationRuleFactory ruleFactory;

    @Before
    public void setUp() throws Exception {
        ruleFactory = new DefaultValidationRuleFactory();
    }

    @Test
    public void ruleFactory() throws Exception {
        TestBean b = new TestBean();
        Collection<ValidationRule> rules = ruleFactory.createFromBean(b, "testBean");
        System.out.println(rules);
        Assert.assertEquals(1, rules.size());
        ValidationRule validationRule = rules.stream().findFirst().get();
        Assert.assertEquals("a.*", validationRule.getRegexp());
        Assert.assertNotNull("size() is null",validationRule.getSize());
        Assert.assertEquals(new Integer[]{-1,4}, validationRule.getSize().toArray());
    }
}
