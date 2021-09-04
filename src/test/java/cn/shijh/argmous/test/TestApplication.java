package cn.shijh.argmous.test;

import cn.shijh.argmous.spring.context.ParamCheck;
import cn.shijh.argmous.spring.context.ParamChecks;
import cn.shijh.argmous.exception.ParamCheckException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

@SpringBootApplication(scanBasePackages = "cn.shijh.argmous")
@RestController
@RequestMapping("/")
public class TestApplication {
    public static void main(String[] args) {
        SpringApplication.run(TestApplication.class, args);
    }

    @RestControllerAdvice
    public static class ErrorHandler {
        @ExceptionHandler(ParamCheckException.class)
        public String error(Exception e) {
            return e.getMessage();
        }
    }

    @GetMapping("/test")
    @ParamChecks({
            @ParamCheck(include = "s", size = {1,3}),
            @ParamCheck(include = "i", range = {"0","5"})
    })
    public String testValidate(String s, Integer i) {
        return "success";
    }
}
