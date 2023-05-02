import org.junit.Test;

import edu.yu.cs.com1320.project.MinHeap;
import edu.yu.cs.com1320.project.impl.MinHeapImpl;

import org.junit.Assert.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.NoSuchElementException;

public class MinHeapImplTest {
    @Test
    public void testInsert1() {
        MinHeap<Integer> heap = new MinHeapImpl<>();
        heap.insert(4);
        heap.insert(2);
        heap.insert(1);
        heap.insert(3);
        assertEquals(1, (int)heap.remove());
        assertEquals(2, (int)heap.remove());
        assertEquals(3, (int)heap.remove());
        assertEquals(4, (int)heap.remove());
    }

    @Test
    public void testRemove() {
        MinHeap<String> heap = new MinHeapImpl<>();
        heap.insert("hello");
        heap.insert("world");
        heap.insert("goodbye");
        assertEquals("goodbye", heap.remove());
        assertEquals("hello", heap.remove());
        assertEquals("world", heap.remove());
        boolean thrown = false;
        try {
            heap.remove();
        } catch (NoSuchElementException e) {
            thrown = true;
        }
        assertTrue(thrown);
    }

    @Test
    public void testReHeapify() {
        MinHeap<Integer> heap = new MinHeapImpl<>();
        heap.insert(4);
        heap.insert(2);
        heap.insert(1);
        heap.insert(3);
        heap.reHeapify(4);
        assertEquals(1, (int)heap.remove());
        assertEquals(2, (int)heap.remove());
        assertEquals(3, (int)heap.remove());
        assertEquals(4, (int)heap.remove());
    }

    @Test
    public void testEmptyHeap() {
        MinHeap<Integer> heap = new MinHeapImpl<>();
        boolean thrown = false;
        try {
            heap.remove();
        } catch (NoSuchElementException e) {
            thrown = true;
        }
        assertTrue(thrown);
    }

    @Test
    public void testResize() {
        MinHeap<Integer> heap = new MinHeapImpl<>();
        for (int i = 0; i < 20; i++) {
            heap.insert(i);
        }
        Integer[] expected = new Integer[20];
        for (int i = 0; i < 20; i++) {
            expected[i] = i;
        }
        Arrays.sort(expected);
        for (int i = 0; i < 20; i++) {
            assertEquals(expected[i], heap.remove());
        }
    }

    @Test
    public void testInsert() {
        MinHeap<Integer> heap = new MinHeapImpl<>();
        heap.insert(4);
        heap.insert(2);
        heap.insert(1);
        heap.insert(3);
        assertEquals(1, heap.remove().intValue());
        assertEquals(2, heap.remove().intValue());
        assertEquals(3, heap.remove().intValue());
        assertEquals(4, heap.remove().intValue());
    }
    
    @Test(expected = NoSuchElementException.class)
    public void testRemoveEmptyHeap() {
        MinHeap<Integer> heap = new MinHeapImpl<>();
        heap.remove();
    }
    
}