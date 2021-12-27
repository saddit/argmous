package top.pressed.argmous.manager;

import top.pressed.argmous.model.ArgumentInfo;
import top.pressed.argmous.model.ValidationRule;

public interface ValidatorManager {
    String validate(ArgumentInfo argument, ValidationRule rule);
}
