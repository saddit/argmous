
> version 1.1.0 has a terrible bug, please wait version 1.1.1

# ARGMOUS

Argmous is a light and easy framework to validate arguments on any method because it dependences on spring-aop and just use annotaion to define validation rules.

:book:[Go to argmous-spring-boot-starter project](https://github.com/838239178/argmous-spring-boot-starter)

:facepunch:[Start on non spring environments](./doc/for_non_spring.md)

## Quick Start

### Method Validation

1. add dependences to your `POM.XML` 

   ```xml
   <dependencies>
       <dependency>
           <groupId>cn.shijh</groupId>
           <artifactId>argmous-spring-boot-starter</artifactId>
           <version>1.1.0-BETA</version>
       </dependency>
   </dependencies>
   ```
   
2. write a controller and handler  `ParamCheckException`

   in this case, we limit length of **s** greater or equal than **1** and less than **3** and limit value of **i** greater or equal than **0** and less than **5** （you can customize the rules of validation)

   *use `@NotValid` to avoid analyzing for some object (will improve performance)*

   ```java
   @SpringBootApplication
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
               @ParamCheck(size = {1,3}, target = "s"),
               @ParamCheck(range = {"0","5"}, target = "i")
       })
       public String testValidate(String s, Integer i, @NotVaid HttpSession session) {
           return "success";
       }
   }
   ```

3. access this api with different params and observe the returned results

   **example:**

   ```
   http://localhost:8080/test?i=0
   http://localhost:8080/test?s=abcdefg&i=0
   http://localhost:8080/test?s=ab&i=100
   ```


### Bean Validation

> Only support on version 1.1.0 or later

1. use annotation like this :arrow_down:

    ```java
    public class TestBean {
        
        @Regexp("a.*")
        @Size({-1,4})
        @Required
        private String name;
    }
    ```

**Remember that bean's validation would be overridden by method annotations**

2. try a case

   ```java
   @SpringBootApplication
   @RestController
   @RequestMapping("/")
   public class TestApplication {
   	
       //...
       
       @GetMapping("/test")
       @ParamChecks({
               @ParamCheck(include = "s", size = {1,3}),
               @ParamCheck(include = "i", range = {"0","5"})
       })
       public String testValidate(String s, Integer i, @NotVaid HttpSession session) {
           return "success";
       }
       
       @GetMapping("/testBean")
       @ParamCheck(include = "name", regexp = "b.*", target = "bean")
       public String testBeanValidate(TestBean bean) {
           return "success";
       }
   }
   ```

   

   **Ex:**

   ```
   http://localhost:8080/testBean?name=abb
   http://localhost:8080/testBean?name=bbcccc
   http://localhost:8080/testBean?name=bcd
   ```

   

## Advanced

### Annotation

| Name            | args                                                         | Note                                                         |
| --------------- | ------------------------------------------------------------ | ------------------------------------------------------------ |
| ParamCheck      | include, size, range, split, regexp, required, target, custom | major annotation. use `custom` to expand validator you want  |
| ParamChecks     | values                                                       | with this annotation, more than two `ParamCheck` can be used  |
| ArrayParamCheck | target, values                                               | If you want to check every elements in an(a) array(list), use this to make `ParamCheck` effect all elements |
| NotValid        |                                                              | use to avoid analyzing and checking for argument             |
| Valid           | value                                                        | for non spring environments. in order to mark argument's name |

> :information_source: not recommend use `ArrayParamCheck` to a large array
>
> :warning: If method have more than one argument you must use `target` on `@ParamCheck` unless you just want to validate first argument 

| Name     | Note                                     |
| -------- | ---------------------------------------- |
| Required | as `ParamCheck::required` , default true |
| Regexp   | as `ParamCheck::regexp`                  |
| Size     | as `ParamCheck::size`                    |
| Range    | as `ParamCheck::range`                   |
| Custom   | as `ParamCheck::custom`                  |
| Split    | as `ParamCheck::split`                   |



### Default Validators

we provide lots of validators

| Name                | Note                                                         |
| :------------------ | :----------------------------------------------------------- |
| RequiredValidator   | make sure arg is not `null` or `""`  (if arg is a string)  if `ParamCheck-required` is true |
| SizeValidator     | check arg with `ParamCheck-size` when size is not empty and arg is array or string. |
| ValueRangeValidator | check arg value with `ParamCheck-ragne`  when range is not empty and arg is number. |
| RegexpValidator     | make sure arg matches the `ParamCheck-regexp` when regexp is not empty and arg is string |

> 1. size {n} and {n, -1} means [n, +∞)
> 2. range {"n"} and {"n", ""} means [n, +∞)

### Expand validator

if you have a special rule,  just implement `RuleValidator` and make it as a component of spring and remember use `ParamCheck-custom`.

```java
public interface RuleValidator {
    /**
     * validate argument
     * @param object argument
     * @param rule rule
     * @return true if passed
     * @throws IllegalStateException if something got wrong
     */
    boolean validate(Object object, ValidationRule rule) throws IllegalStateException;

    /**
     * return the notify message of an no passed validating
     * @param rule rule
     * @return notify message
     */
    String errorMessage(ValidationRule rule);

    /**
     * does support to be checked ?
     * @param paramType argument's type
     * @param rule rule
     * @return true if supported
     */
    boolean support(Class<?> paramType, ValidationRule rule);
}
```

> we provide `CustomizeUtils` to help solve custom args

## Architecture

![Architecture](https://cdn.jsdelivr.net/gh/838239178/PicgoBed@main/img/%E6%9C%AA%E5%91%BD%E5%90%8D%E6%96%87%E4%BB%B6%20(1).jpg)
