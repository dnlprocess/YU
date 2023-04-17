package edu.yu.cs.com1320.project.impl;

import edu.yu.cs.com1320.project.Stack;
/**
 * @param <T>
 */
public class StackImpl<T> implements Stack<T> {

    private class Node<T> {
        T element;
        Node<T> next;

        Node(T element, Node<T> next) {
            this.element = element;
            this.next = next;
        }
    }


    private Node<T> top;

    public StackImpl() {
        this.top = null;
    }
    
    /**
     * @param element object to add to the Stack
     */
    public void push(T element) {
        if (!(element == null)) {
            Node<T> newNode = new Node<T>(element, this.top);
            this.top = newNode;
        }
    }

    /**
     * removes and returns element at the top of the stack
     * @return element at the top of the stack, null if the stack is empty
     */
    public T pop() {
        if (this.top == null) {
            return null;
        }

        T temp = this.top.element;

        this.top = this.top.next;
        return temp;
    }

    /**
     *
     * @return the element at the top of the stack without removing it
     */
    public T peek() {
        return this.top == null? null: this.top.element;
    }

    /**
     *
     * @return how many elements are currently in the stack
     */
    public int size() {
        int count = 0;
        Node<T> current = this.top;
        while(current != null) {
            count++;
            current = current.next;
        }
        return count;
    }
}
