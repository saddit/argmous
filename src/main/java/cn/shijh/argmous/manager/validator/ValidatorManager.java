package cn.shijh.argmous.manager.validator;

import cn.shijh.argmous.model.ArgumentInfo;
import cn.shijh.argmous.model.ValidationRule;

public interface ValidatorManager {
    String validate(ArgumentInfo argument, ValidationRule rule);
}
