package top.pressed.argmous;

import top.pressed.argmous.annotation.bean.Regexp;
import top.pressed.argmous.annotation.bean.Size;
import lombok.Data;

@Data
public class TestBean {

    @Regexp("a.*")
    @Size({-1,4})
    private String name;
    
    private Integer num;

    @Size({-1,10})
    private Integer none;
}
