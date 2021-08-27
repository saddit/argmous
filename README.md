# ARGMOUS

Argmous is a light and easy framework to validate arguments on any method because it dependences on spring-aop and just use annotaion to define validation rules.

## Quick Start

1. add dependences to your `POM.XML` 

   ```xml
   <dependency>
       <groupId>cn.shijh</groupId>
       <artifactId>argmous</artifactId>
   </dependency>
   <dependency>
       <groupId>org.springframework.boot</groupId>
       <artifactId>spring-boot-starter-web</artifactId>
   </dependency>
   <dependency>
       <groupId>org.projectlombok</groupId>
       <artifactId>lombok</artifactId>
       <optional>true</optional>
   </dependency>
   <dependency>
       <groupId>org.springframework.boot</groupId>
       <artifactId>spring-boot-starter-aop</artifactId>
   </dependency>
   ```

2. write a controller and handler  `ParamCheckException`

   in this case, we limit length of **s** greater or equal than **1** and less than **3** and limit value of **i** greater or equal than **0** and less than **5** （you can customize the rules of validation)

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
       public String testValidate(String s, Integer i) {
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

