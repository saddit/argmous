package cn.shijh.argmous;

import cn.shijh.argmous.annotation.bean.Regexp;
import cn.shijh.argmous.annotation.bean.Size;
import lombok.Data;

@Data
public class TestBean {

    @Regexp("a.*")
    @Size({-1,4})
    private String name;
}
