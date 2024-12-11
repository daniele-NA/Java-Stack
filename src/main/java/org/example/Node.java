package org.example;

import java.io.Serializable;

/**
 * this class is used for the functioning of stacks and queues,
 * since it hooks memory locations and allows a value inside it
 * <blockquote><pre>
 *    final Node &lt;String&gt; node = new Node &lt;&gt;() ;
 * </pre></blockquote>
 * <p>
 * Obviously the class doesn't force you to use try/catch
 *
 * @param <T> – the type of the Node
 * @author Crescenzi Daniele
 * @see Serializable
 */


public class Node<T> implements Serializable {

    /**
     * the value for each node
     */
    private T value;
    /**
     * the {@code next node} [location of memory] for this node
     */
    private Node<T> nextNode = null;

    /**
     * Constructor who assign the value
     * @param value {@code T}
     */
    public Node(T value) {
        if (value == null) {
            throw new IllegalArgumentException("null value");
        }

        this.value = value;
    }

    /**
     * Default getter
     * @return {@code T}
     */
    public T getValue() {
        return value;
    }

    /**
     * Default setter with null check
     * @param value {@code T value}
     */
    public void setValue(T value) {
        if (value == null) {
            throw new IllegalArgumentException("null value");
        }
        this.value = value;
    }

    /**
     * Default getter for the next node
     * @return {@code T}
     */
    public Node<T> getNextNode() {
        return nextNode;
    }

    /**
     * Default setter
     * @param nextNode {@code Node <T>}
     */
    public void setNextNode(Node<T> nextNode) {
        this.nextNode = nextNode;
    }

}
