package top.pressed.argmous.handler;


import lombok.var;

import java.util.*;
import java.util.function.BiConsumer;

/**
 * @author Administrator
 * @version 1.0
 */
public class SpecTopologyGraph<T> {

    private final Queue<SpecTopologyNode<T>> nodes;

    public SpecTopologyGraph() {
        nodes = new PriorityQueue<>(Comparator.comparingInt(SpecTopologyNode::getPreNodeCount));
    }

    private SpecTopologyNode<T> addNew(T value) {
        var n = new SpecTopologyNode<>(value, null);
        nodes.add(n);
        return n;
    }

    public void add(T edgeStart, T edgeEnd) {
        SpecTopologyNode<T> start = Optional.ofNullable(get(edgeStart))
                .orElseGet(() -> addNew(edgeStart));
        SpecTopologyNode<T> end = Optional.ofNullable(get(edgeEnd))
                .orElseGet(() -> addNew(edgeEnd));
        start.setAfterNode(end);
        end.preNodeCountUp();
    }

    public SpecTopologyNode<T> get(T value) {
        for (var node : nodes) {
            if (node.getValue().equals(value)) {
                return node;
            }
        }
        return null;
    }

    public SpecTopologyNode<T> peek() {
        return nodes.peek();
    }

    public boolean isEmpty() {
        return nodes.isEmpty();
    }

    public T popEach(BiConsumer<T, T> consumer) {
        SpecTopologyNode<T> node = peek();
        while (node.getAfterNode() != null) {
            if (node.getPreNodeCount() == 0) {
                SpecTopologyNode<T> nextNode = pop(node);
                consumer.accept(node.getValue(), nextNode.getValue());
                node = nextNode;
            } else {
                node = peek();
            }
        }
        consumer.accept(node.getValue(), null);
        pop(node);
        return node.getValue();
    }

    public void popEach2(BiConsumer<T, T> consumer) {
        Deque<SpecTopologyNode<T>> stack = new ArrayDeque<>();
        for (SpecTopologyNode<T> node : nodes) {
            if (node.getPreNodeCount() == 0) {
                stack.push(node);
            }
        }
        while (!stack.isEmpty()) {
            SpecTopologyNode<T> node = stack.pop();
            SpecTopologyNode<T> afterNode = node.getAfterNode();
            if (afterNode != null) {
                afterNode.preNodeCountDown();
                if (afterNode.getPreNodeCount() == 0) {
                    stack.push(afterNode);
                }
                consumer.accept(node.getValue(), afterNode.getValue());
            } else {
                consumer.accept(node.getValue(), null);
            }
        }
    }

    /**
     * remove node from graph and return its next node
     */
    public SpecTopologyNode<T> pop(SpecTopologyNode<T> node) {
        SpecTopologyNode<T> afterNode = node.getAfterNode();
        nodes.remove(node);
        if (afterNode != null) {
            afterNode.preNodeCountDown();
        }
        return afterNode;
    }
}