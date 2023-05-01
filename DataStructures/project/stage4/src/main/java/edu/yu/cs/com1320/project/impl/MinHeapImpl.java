package edu.yu.cs.com1320.project.impl;

import edu.yu.cs.com1320.project.MinHeap;
import java.lang.reflect.Array;

import java.util.NoSuchElementException;

public class MinHeapImpl<E extends Comparable<E>> extends MinHeap<E> {

    @Override
    public void reHeapify(E element) {
        int index = getArrayIndex(element);
        
        upHeap(index);
        downHeap(index);
    }

    @Override
    /**
     * get the index of the element within the array. If it is not present throw NoSuchElementException
     */
    protected int getArrayIndex(E element) {
        int count = 0;
        for (E currentElement: this.elements) {
            if (currentElement.equals(element)) {
                break;
            }
            count++;
        }

        if (count == this.elements.length) {
            throw new NoSuchElementException("Element not in heap!");
        }
        return count;
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void doubleArraySize() {
        E[] tempElements = (E[]) new Comparable[2*this.count];
        E element;
        for (int i=0; i<this.count; i++) {
            element = this.elements[i];
            tempElements[i] = element;
        }

        this.elements = tempElements;
    }
    
}
