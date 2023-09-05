package edu.yu.cs.com1320.project.impl;

import edu.yu.cs.com1320.project.MinHeap;

import edu.yu.cs.com1320.project.stage5.impl.DocumentStoreImpl;
import edu.yu.cs.com1320.project.stage5.impl.DocumentStoreImpl.URIUseTimeComparator;
/*
import java.lang.reflect.Array;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
*/
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

        System.out.println("Before: \n");
        printHeap();

        if (element.getClass() == URIUseTimeComparator.class) {
            System.out.println("reheapify: " + ((URIUseTimeComparator) element).uri.toString());
        }

        int index = getArrayIndex(element);
        elements[index] = element;
        
        this.elements[index] = element;

        upHeap(index);
        index = getArrayIndex(element);
        downHeap(index);

        System.out.println("After: \n");
        printHeap();
    }

    @Override
    /**
     * get the index of the element within the array. If it is not present throw NoSuchElementException
     */
    protected int getArrayIndex(E element) {// fix_______________
        for (int i=1; i<=this.count; i++) {
            if (this.elements[i].equals(element)) {
                if (element.getClass() == URIUseTimeComparator.class) {
                    System.out.println("matches: " + ((URIUseTimeComparator) elements[i]).uri.toString());
                }
                System.out.println(i);
                return i;
            }
        }
        
        /*
        try {
            if (((URIUseTimeComparator) element).uri.equals(new URI("http://edu.yu.cs/com1320/project/doc1"))) {
                throw new IllegalArgumentException();
            }
        } catch (URISyntaxException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        */
        if (element.getClass() == DocumentStoreImpl.URIUseTimeComparator.class) {
            throw new NoSuchElementException(((URIUseTimeComparator) element).getURI().toString());
        }
        
        throw new NoSuchElementException("Element not in heap!: " + element.toString());
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void doubleArraySize() {
        E[] tempElements = (E[]) new Comparable[2*this.count];

        for (int i = 1; i <= this.count; i++) {
            tempElements[i] = elements[i];
            System.out.println("organize " + tempElements[i].toString());
        }

        this.elements = tempElements;
    }

    //-------------------------

    public void printHeap() {
        for (E element: this.elements) {
            if (element != null && element.getClass() == URIUseTimeComparator.class) {
                System.out.println(((URIUseTimeComparator) element).uri.toString());
            }
        }
    }

}
