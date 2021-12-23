package top.pressed.argmous.handler;


import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Administrator
 * @version 1.0
 */
@Getter
@Setter
@EqualsAndHashCode
public class SpecTopologyNode<T> {

    private SpecTopologyNode<T> afterNode;
    private int preNodeCount;
    private T value;

    public SpecTopologyNode(T value, SpecTopologyNode<T> after) {
        this.value = value;
        this.afterNode = after;
        this.preNodeCount = 0;
    }

    public void preNodeCountUp() {
        preNodeCount++;
    }

    public void preNodeCountDown() {
        preNodeCount--;
    }

    @Override
    public String toString() {
        return "SpecTopologyNode{" +
                "afterNode.value=" + (afterNode == null ? "null" : afterNode.getValue()) +
                ", preNodeCount=" + preNodeCount +
                ", value=" + value +
                '}';
    }
}