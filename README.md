# ARGMOUS

Argmous is a light and easy framework to validate arguments on any method because it dependences on spring-aop and just use annotaion to define validation rules.

:book:[Go to argmous-spring-boot-starter project](https://github.com/838239178/argmous-spring-boot-starter)

:facepunch:[Start on non spring environments](./doc/for_non_spring.md)

## Quick Start

1. add dependences to your `POM.XML` 

   ```xml
   <dependencies>
       <dependency>
           <groupId>cn.shijh</groupId>
           <artifactId>argmous-spring-boot-starter</artifactId>
           <version>1.0.2-RELEASE</version>
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
               @ParamCheck(include = "s", size = {1,3}),
               @ParamCheck(include = "i", range = {"0","5"})
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

   

## Advanced

### Annotation

| Name            | args                                                         | Note                                                         |
| --------------- | ------------------------------------------------------------ | ------------------------------------------------------------ |
| ParamCheck      | include, exclude, size, range, split2array ,split, regexp, required, target, custom | major annotation. use `custom` to expand validator you want  |
| ParamChecks     | values                                                       | with this annotaion, more than two `ParamCheck` can be used  |
| ArrayParamCheck | target, values                                               | If you want to check every elements in an(a) array(list), use this to make `ParamCheck` effect all elements |
| NotValid        |                                                              | use to avoid analyzing and checking for argument             |
| Valid           | value                                                        | for non spring environments. in order to mark argument's name |

> :information_source: not recommend use `ArrayParamCheck` to a large array

### Default Validators

we provide lots of validators

| Name                | Note                                                         |
| :------------------ | :----------------------------------------------------------- |
| RequiredValidator   | make sure arg is not `null` or `""`  (if arg is a string)  if `ParamCheck-required` is true |
| SizeValidatator     | check arg with `ParamCheck-size` when size is not empty and arg is array or string. |
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
