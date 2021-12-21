package top.pressed.argmous.toplogy;

import org.junit.Before;
import org.junit.Test;
import top.pressed.argmous.handler.SpecTopologyGraph;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicReference;

public class GraphTestCase {

    SpecTopologyGraph<String> graph;
    HashMap<String, String> map;

    @Before
    public void setUp() {
        graph = new SpecTopologyGraph<>();
        map = new HashMap<>();
        map.put("A", "A");
        map.put("B", "B");
        map.put("C", "C");
        map.put("D", "D");
        map.put("E", "E");
        graph.add("A", "B");
        graph.add("B", "C");
        graph.add("D", "C");
        graph.add("C", "E");
    }

    @Test
    public void test1() {
        AtomicReference<String> finalValue = new AtomicReference<>();
        long s = System.currentTimeMillis();
        graph.popEach((v1, v2) -> {
            String value = map.get(v1);
            if (v2 != null) {
                map.put(v2, map.get(v1) + "," + map.get(v2));
            } else {
                finalValue.set(value);
            }
        });
        System.out.println(System.currentTimeMillis() - s);
        System.out.println(finalValue.get());
    }

    @Test
    public void test2() {
        AtomicReference<String> finalValue = new AtomicReference<>();
        long s = System.currentTimeMillis();
        graph.popEach2((v1, v2) -> {
            String value = map.get(v1);
            if (v2 != null) {
                map.put(v2, map.get(v1) + "," + map.get(v2));
            } else {
                finalValue.set(value);
            }
        });
        System.out.println(System.currentTimeMillis() - s);
        System.out.println(finalValue.get());
    }
}
