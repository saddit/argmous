package top.pressed.argmous.manager.validator;

import top.pressed.argmous.model.ArgumentInfo;
import top.pressed.argmous.model.ValidationRule;

public interface ValidatorManager {
    String validate(ArgumentInfo argument, ValidationRule rule);
}
