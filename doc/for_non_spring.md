# Argmous

this page is a guide for people who want to use Argmous on non spring environments.

## Quick Start

1. add dependencies to your `POM.XML`

   ```xml
   <dependencies>
       <dependency>
           <groupId>top.pressed</groupId>
           <artifactId>argmous</artifactId>
           <version>1.1.3-BETA</version>
       </dependency>
   </dependencies>
   ```

2. create an instance of `ArgmousProxyFactory`. the default implementation class is `JDKProxyFactory`.

   ```java
   import top.pressed.argmous.ArgmousInitializr;    
   public class Solution {
       private ArgmousProxyFactory proxyFactory = ArgmousInitializr.getProxyFactory(); 
   }
   ```

3. use `proxyFactory` to create object then it can be automatic checked if methods are annotated by `@Paramcheck`、`@Paramcheks` or `@ArrayParamCheck`

   ```java
   public class Solution {
       private ArgmousProxyFactory proxyFactory = ArgmousInitializr.getProxyFactory();
       private TestService testService = (TestService) proxyFactory.proxy(new TestServiceImpl());
   }
   ```

> :warning: The parameters of method should be annotated by `@Valid("name")` to mark them name or else they will use default name like arg0, arg1 and so on.

## Add Custom Validator

if you want to add your custom validators you can init them when build ArgmousProxyFactory

```java
import top.pressed.argmous.ArgmousInitializr;

public class Solution {
   public Solution() {
      ArgmousInitializr.addValidators(new CustomValidator());
   }
}
```

## ArgmousInitializr

`ArgmousInitializr` is the entry of argmous. You can use `initBean` and `finishInit` to custom all components of argmous
most of the time using `defaultInit` and `addValidators` is enough.

if `initBean` has been invoked, ensure `finishBean` invoked after that and all components should init by yourself
include `ArgumentInfoFactory`, `ValidationRuleFactory`, `ValidationManager`, `ValidatorManager`, `ArgmousService`,
`RuleMixHandler` and `ArgmousProxyFactory`

