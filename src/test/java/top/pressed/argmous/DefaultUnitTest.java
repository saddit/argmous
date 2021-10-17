package top.pressed.argmous;

import org.junit.Before;
import org.junit.Test;
import top.pressed.argmous.exception.ParamCheckException;
import top.pressed.argmous.factory.ArgmousProxyFactory;

import java.util.Arrays;
import java.util.Collections;

@SuppressWarnings("all")
public class DefaultUnitTest {
    private TargetService targetService;

    @Before
    public void setUp() throws Exception {
        ArgmousProxyFactory jdkProxyFactory = ArgmousProxyFactory.builder().build();
        //targetService = (TargetService) jdkProxyFactory.proxy(new TargetServiceImpl());
        targetService = (TargetService) jdkProxyFactory.newProxyInstance(TargetServiceImpl.class);
    }

    @Test
    public void allPass() throws Exception {
        String s = "ab";
        Integer i = 2;
        targetService.test1(s);
        targetService.test2(s, i);
        targetService.test3(Arrays.asList(s, s));
    }

    @Test
    public void test1error() throws Exception {
        String s = "ab";
        String s1 = "bcc";
        Integer i = 2;
        try {
            targetService.test1(s1);
            throw new IllegalStateException("test fail: test1 passed");
        } catch (ParamCheckException e) {
            System.out.println("test1 error->" + e.getMessage());
        }
        try {
            targetService.test1("abbbb");
            throw new IllegalStateException("test fail: test1 passed");
        } catch (ParamCheckException e) {
            System.out.println("test1 error->" + e.getMessage());
        }
        targetService.test2(s, i);
        targetService.test3(Arrays.asList(s, s));
    }

    @Test
    public void test2error() throws Exception {
        String s = "ab";
        Integer i = 3;
        targetService.test1(s);
        try {
            targetService.test2(s, i);
            throw new IllegalStateException("test fail: test2 passed");
        } catch (ParamCheckException e) {
            System.out.println("test2 error->" + e.getMessage());
        }
        targetService.test3(Arrays.asList(s, s));
    }

    @Test
    public void test3error() throws Exception {
        String s = "cbcd";
        String s1 = "a";
        Integer i = 2;
        targetService.test1("ab");
        targetService.test2(s, i);
        try {
            targetService.test3(Arrays.asList(s, s1));
            throw new IllegalStateException("test fail: test3 passed");
        } catch (ParamCheckException e) {
            System.out.println("test3-1 error->" + e.getMessage());
        }

        try {
            targetService.test3(Arrays.asList(s1, s));
            throw new IllegalStateException("test fail: test3 passed");
        } catch (ParamCheckException e) {
            System.out.println("test3-2 error->" + e.getMessage());
        }
    }

    @Test
    public void testNullArray() throws Exception {
        try {
            targetService.test3(null);
            throw new IllegalStateException("test fail: test3 passed");
        } catch (ParamCheckException e) {
            System.out.println("test3-1 error->" + e.getMessage());
        }
    }

    @Test
    public void testEmptyArray() throws Exception {
        try {
            targetService.test3(Collections.emptyList());
            throw new IllegalStateException("test fail: test3 passed");
        } catch (ParamCheckException e) {
            System.out.println("test3-1 error->" + e.getMessage());
        }
    }

    @Test
    public void testBean() throws Exception {
        TestBean testBean = new TestBean();
        testBean.setName("a12");
        targetService.testBean(testBean);
        testBean.setName("b12");
        targetService.testBeanOverride(testBean);
        try {
            testBean.setName("ccc");
            targetService.testBean2(testBean, "a");
            throw new IllegalStateException("test fail: test passed");
        } catch (ParamCheckException p) {
            System.out.println("testBean-3 error->" + p.getMessage());
        }
        try {
            testBean.setName("abc");
            targetService.testBean2(testBean, "qweqweq");
            throw new IllegalStateException("test fail: test passed");
        } catch (ParamCheckException p) {
            System.out.println("testBean-4 error->" + p.getMessage());
        }
    }

    @Test
    public void testBean3() throws Exception {
        TestBean testBean = new TestBean();
        testBean.setName("abb");
        testBean.setNum(11);
        try {
            targetService.test4(testBean);
            throw new IllegalStateException("test fail: test passed");
        } catch (ParamCheckException p) {
            System.out.println("testBean3 error->" + p.getMessage());
        }
    }

    @Test
    public void testBeanArray() throws Exception {
        TestBean b1 = new TestBean();
        b1.setNum(13);
        TestBean b2 = new TestBean();
        b2.setNum(10);
        TestBean b3 = new TestBean();
        b3.setNone(1);
        try {
            targetService.testBeanArray(Arrays.asList(b1, b2), b3);
            throw new IllegalStateException("test fail: test passed");
        } catch (ParamCheckException p) {
            System.out.println("testBeanArray-1 error->" + p.getMessage());
        }
        b1.setNum(10);
        targetService.testBeanArray(Arrays.asList(b1, b2), b3);
        try {
            b3.setNone(10);
            targetService.testBeanArray(Arrays.asList(b1, b2), b3);
            throw new IllegalStateException("test fail: test passed");
        } catch (ParamCheckException p) {
            System.out.println("testBeanArray-2 error->" + p.getMessage());
        }
    }
}
