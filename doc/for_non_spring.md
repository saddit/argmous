# Argmous

this page is a guide for people who want to use Argmous on non spring environments.

## Quick Start

1. add dependences to your `POM.XML` 

   ```xml
   <dependencies>
       <dependency>
           <groupId>cn.shijh</groupId>
           <artifactId>argmous</artifactId>
           <version>1.1.0-BETA</version>
       </dependency>
   </dependencies>
   ```

2. create an instance of `ArgmousProxyFactory`. the default implementation class is `JDKProxyFactory`.

   ```java
   public class Solution {
       private ArgmousProxyFactory proxyFactory = ArgmousProxyFactory.builder().build(); 
   }
   ```

3. use `proxyFactory` to create object then it can be automatic checked if methods are annotated by `@Paramcheck`、`@Paramcheks` or `@ArrayParamCheck`

   ```java
   public class Solution {
       private ArgmousProxyFactory proxyFactory = ArgmousProxyFactory.builder().build();
       private TestService testService = (TestService) proxyFactory.proxy(new TestServiceImpl());
   }
   ```

> :warning: The parameters of method should be annotated by `@Valid("name")` to mark them name or else they will use default name like arg0, arg1 and so on.

## Add Custom Validator

if you want to add your custom validators you can init them when build ArgmousProxyFactory

```java
public class Solution {
    private ArgmousProxyFactory proxyFactory = ArgmousProxyFactory.builder()
        .addValidator(new MyCustomValidator())
        .build(); 
}
```

