package org.example;


import java.io.Serializable;
import java.util.Iterator;
import java.util.Objects;
import java.util.Spliterator;
import java.util.function.Consumer;

/**
 * <p>The {@code Stack} class is useful for data storage</p>
 * The class follows the {@code LIFO} policy, therefore it behaves differently from that of the java.util Collections
 * The class is iterable only through the .forEach(Consumer...) method
 * You have 17 method that you can use for:
 * {@code Pop elements}, {@code Push elements}
 * and all the basic actions of a java.util stack
 * <blockquote><pre>
 *    final Stack &lt;String&gt; stack = new Stack &lt;&gt;() ;
 * </pre></blockquote>
 * <p>
 * If the stack is empty ,it will return {@link AssertionError},
 * </p>
 * Obviously the class doesn't force you to use try/catch
 * You can declare an immutable object of type stack with this syntax [String list]:
 * This entire stack is supported by the first node, of class {@code Node}
 *
 * @param <E> – the type of elements in this list
 * @author Crescenzi Daniele
 * @see org.example.Node
 * @see Iterable
 * @see Spliterator
 * @see Iterator
 * @see Serializable
 */

public final class Stack<E> implements Iterable<E>, Serializable {

    /**
     * empty constructor
     */
    public Stack(){

    }

    /**
     * Declared as {@code volatile} for access by multiple threads at the same time
     * all the others are hooked onto this {@code firstNode}, each node contains a subsequent node variable
     */
    private volatile Node<E> firstNode = null; //it starts with empty stack

    private transient E[] elementData; //excluded from the serialization


    /**
     * In case of an empty stack, with reflection you can understand the calling method [private]
     */
    private void _isEmpty() {
        String callerMethodName = Thread.currentThread().getStackTrace()[2].getMethodName();  //take the name of the calling method
        if (firstNode == null) {
            throw new AssertionError(String.format("You cannot call %s when the stack is empty", callerMethodName));
        }

    }

    /**
     * public method that say if the stack is empty
     * @return {@code boolean}
     */
    public boolean isEmpty() {
        return firstNode == null;
    }

    /**
     * return how much values there are into the stack
     * @return {@code int}
     */

    public int length() {
        Node<E> currentNode = firstNode;
        int length = 0;
        while (currentNode != null) {
            length++;
            currentNode = currentNode.getNextNode();
        }
        return length;
    }

    /**
     * it clears all the stack
     */

    public void clear() {
        firstNode = null;
    }

    /**
     * reassigns all stack nodes
     * <blockquote><pre>
     *      stack.addAll(new Stack &lt;String&gt;());
     *   </pre></blockquote>
     *
     * @param newStack new stack replacing the old one
     */

    public void addAll(Stack<E> newStack) {
        firstNode = newStack.firstNode;
    }

    /**
     * you can insert an element that will go to the top of the stack
     * @param value ,the same type that brings the stack into declaration
     */

    public void push(E value) {
        Node<E> newNode = new Node<>(value);
        newNode.setNextNode(firstNode);
        firstNode = newNode;
    }

    /**
     * it will set the new value at specific position
     * @param index ,enter the index of the element, but always starting from 0
     * @param value ,the new value
     */

    public void set(int index, E value) {
        /*
      we do internalIndex EQUALS TO index because from the outside if I pass 2 for example, I am in third position
        */
        _isEmpty();
        if (index >= length()) {
            throw new IndexOutOfBoundsException("Index out of bounds");
        }
        Node<E> currentNode = firstNode;
        int internalIndex = 0;
        while (currentNode != null) {
            if (internalIndex == index) {
                currentNode.setValue(value);
                break;
            }
            currentNode = currentNode.getNextNode();
            internalIndex++;
        }
    }

    /**
     * extract the last inserted element
     *
     * @return {@code E}
     */
    public E pop() {
        _isEmpty();
        Node<E> currentNode = firstNode;
        firstNode = currentNode.getNextNode();
        return (E) currentNode.getValue();
    }

    /**
     * this method return the value that you required with the index
     * @param index ,the index where you want to get its {@code Current Value}
     * @return {@code E}
     */

    public E get(int index) {
        int internalIndex = 0;
        _isEmpty();
        Node<E> currentNode = firstNode;

        while (internalIndex < index) {
            currentNode = currentNode.getNextNode();
            internalIndex++;
        }
        return currentNode.getValue();
    }

    /**
     * it takes the last element that you put into it
     *
     * @return {@code E}
     */
    public E getFirst() {
        _isEmpty();
        return firstNode.getValue();
    }

    /**
     * it takes the first element that you put into it
     *
     * @return {@code E}
     */
    public E getLast() {
        _isEmpty();
        Node<E> currentNode = firstNode;
        while (currentNode != null) {
            if (currentNode.getNextNode() == null) {
                return currentNode.getValue();
            }
            currentNode = currentNode.getNextNode();
        }
        return null;
    }

    /**
     * this method check if there is a specific value
     * @param value ,the value to look for
     * @return {@code TRUE} if the value is into the stack, else it will return {@code FALSE}
     */
    public boolean contains(E value) {
        _isEmpty();
        Node<E> currentNode = firstNode;
        while (currentNode != null) {
            if (currentNode.getValue().equals(value)) {
                return true;
            }
            currentNode = currentNode.getNextNode();
        }
        return false;
    }

    /**
     * it will return the {@code FIRST INDEX} of the position of that value
     * @param value ,the value to look for
     * @return int
     */

    public int indexOf(E value) {
        _isEmpty();

        Node<E> currentNode = firstNode;
        int internalIndex = 0;
        while (internalIndex < length()) {
            if (currentNode.getValue().equals(value)) {
                return internalIndex;
            }
            currentNode = currentNode.getNextNode();
            internalIndex++;
        }
        return -1;
    }

    /**
     * This is an overloaded method, one enters an index, and the other a {@code E} value
     * This method is {@code synchronized},because you can access it from multiple and different threads
     *
     * @param value ,the value to look for
     * @return {@code TRUE} if the value has been removed from the stack, else it will return {@code FALSE}
     */
    @Overloaded
    public synchronized boolean remove(E value) {
        _isEmpty();

        Node<E> currentNode = firstNode;
        Node<E> previousNode = null;
        if (firstNode.getValue().equals(value)) { //check if the value it's in the first position[actually the last]
            firstNode = firstNode.getNextNode();
            return true;
        }
        while (currentNode != null) {
            if (currentNode.getValue().equals(value) && previousNode != null) {
                previousNode.setNextNode(currentNode.getNextNode());

                return true;
            }
            previousNode = currentNode;
            currentNode = currentNode.getNextNode();
        }
        return false;
    }

    /**
     * This is an overloaded method, one enters an index, and the other a {@code E} value
     *
     * @param index ,the index where you want to remove its {@code Current Value}
     * @return if the value has been removed from the stack, else it will return {@code FALSE}
     */
    @Overloaded
    public boolean remove(int index) {
        _isEmpty();
        if (index >= length()) {
            throw new IndexOutOfBoundsException("Index out of bounds");
        }
        /*
        it will save the current node,and also the previous node
         */
        Node<E> previousNode = null;
        Node<E> currentNode = firstNode;
        int internalIndex = 0;
        while (currentNode != null) {
            if (internalIndex == index) {
                if (internalIndex == 0) {
                    firstNode = currentNode.getNextNode();
                    return true;
                }
                previousNode.setNextNode(currentNode.getNextNode());
                return true;
            }
            previousNode = currentNode;
            currentNode = currentNode.getNextNode();
            internalIndex++;
        }
        return false;
    }

    /**
     * will remove the last object you placed in it
     */
    public synchronized void removeFirst() {
        _isEmpty();
        if (firstNode.getNextNode() != null) {
            firstNode = firstNode.getNextNode();
        } else firstNode = null;
    }

    /**
     * will remove the first object you placed in it
     */
    public void removeLast() {
        _isEmpty();
        Node<E> currentNode = firstNode;
        Node<E> previousNode = null;
        while (currentNode != null) {
            if (currentNode.getNextNode() == null && previousNode != null) {
                previousNode.setNextNode(currentNode.getNextNode());
                break;

            }
            previousNode = currentNode;
            currentNode = currentNode.getNextNode();
        }
    }

    //overrides the method of the object class
    @Override
    public String toString() {
        _isEmpty();
        final StringBuilder sb = new StringBuilder();
        Node<E> currentNode = firstNode;
        while (currentNode != null) {
            sb.append(currentNode.getValue()).append(System.lineSeparator());
            currentNode = currentNode.getNextNode();

        }
        return sb.toString();
    }

    /**
     * Inherited method
     * @return {@code NULL}
     */
    @Override
    public Iterator<E> iterator() {
        return null;
    }

    /**
     * applies the input consumer to the entire stack
     * @param action The action to be performed for each element
     */

    @Override
    public void forEach(Consumer<? super E> action) {
        Objects.requireNonNull(action);
        elementData = (E[]) new Object[length()];
        int index = 0;
        Node<E> currentNode = firstNode;
        while (currentNode != null) {
            elementData[index] = currentNode.getValue();
            currentNode = currentNode.getNextNode();
            index++;

        }
        index = 0;
        currentNode = firstNode;
        while (currentNode != null) {
            action.accept(elementData[index]);
            currentNode = currentNode.getNextNode();
            index++;
        }
    }

    /**
     * Inherited method
     * @return {@code NULL}
     */
    @Override
    public Spliterator<E> spliterator() {
        return null;
    }
}

