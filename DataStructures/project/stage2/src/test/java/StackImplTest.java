import org.junit.Test;

import static org.junit.Assert.*;

import edu.yu.cs.com1320.project.impl.StackImpl;

public class StackImplTest {

    @Test
    public void testPushAndPop() {
        StackImpl<String> stack = new StackImpl<String>();
        assertTrue(stack.size() == 0);

        stack.push("a");
        assertTrue(stack.size() == 1);
        assertEquals("a", stack.pop());
        assertTrue(stack.size() == 0);

        stack.push("b");
        stack.push("c");
        assertTrue(stack.size() == 2);
        assertEquals("c", stack.pop());
        assertTrue(stack.size() == 1);
        assertEquals("b", stack.pop());
        assertTrue(stack.size() == 0);
    }

    @Test
    public void testPeek() {
        StackImpl<String> stack = new StackImpl<String>();
        assertNull(stack.peek());

        stack.push("a");
        assertEquals("a", stack.peek());

        stack.push("b");
        assertEquals("b", stack.peek());

        assertEquals("b", stack.pop());
        assertEquals("a", stack.peek());

        stack.pop();
        assertNull(stack.peek());
    }

    @Test
    public void testSize() {
        StackImpl<Integer> stack = new StackImpl<Integer>();
        assertTrue(stack.size() == 0);

        stack.push(1);
        assertTrue(stack.size() == 1);

        stack.push(2);
        assertTrue(stack.size() == 2);

        stack.pop();
        assertTrue(stack.size() == 1);

        stack.pop();
        assertTrue(stack.size() == 0);
    }
}