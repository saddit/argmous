package top.pressed.argmous.model;

import lombok.*;

import java.util.ArrayList;
import java.util.Collection;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ValidationRule {
    private String regexp;

    private String split;

    private Collection<Integer> size;

    private Collection<String> range;

    private Boolean required;

    private String target;

    private Collection<String> include;

    private Collection<String> custom;

    public ValidationRule(String target) {
        this.target = target;
    }

    public void addInclude(String s) {
        if (include == null) {
            include = new ArrayList<>(1);
        }
        include.add(s);
    }

    public String getFirstInclude() {
        if (include == null || include.isEmpty()) {
            return null;
        }
        return include.stream().findFirst().orElse("");
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public static ValidationRule empty() {
        return new ValidationRule("", "",
                new ArrayList<>(),
                new ArrayList<>(),
                false,
                "",
                new ArrayList<>(),
                new ArrayList<>());
    }
}
