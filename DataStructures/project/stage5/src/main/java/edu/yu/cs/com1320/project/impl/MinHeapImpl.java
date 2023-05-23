package edu.yu.cs.com1320.project.impl;

import edu.yu.cs.com1320.project.MinHeap;
import java.lang.reflect.Array;

import java.util.NoSuchElementException;

public class MinHeapImpl<E extends Comparable<E>> extends MinHeap<E> {

    @SuppressWarnings("unchecked")
    public MinHeapImpl() {
        this.elements = (E[]) new Comparable[3];
        this.count =0;
    }

    @Override
    public void reHeapify(E element) {
        if (element == null) {
            throw new NoSuchElementException();
        }

        int index = getArrayIndex(element);
        
        upHeap(index);
        index = getArrayIndex(element);
        downHeap(index);
    }

    @Override
    /**
     * get the index of the element within the array. If it is not present throw NoSuchElementException
     */
    public int getArrayIndex(E element) {// fix_______________
        for (int i=1; i<=this.count; i++) {
            if (this.elements[i].equals(element)) {
                return i;
            }
        }

        throw new NoSuchElementException("Element not in heap!");
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void doubleArraySize() {
        E[] tempElements = (E[]) new Comparable[2*this.count];

        for (int i = 1; i <= this.count; i++) {
            tempElements[i] = elements[i];
        }

        this.elements = tempElements;
    }

}
