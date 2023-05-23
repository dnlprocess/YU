import edu.yu.cs.com1320.project.BTree;
import edu.yu.cs.com1320.project.impl.BTreeImpl;
import edu.yu.cs.com1320.project.stage5.PersistenceManager;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class BTreeImplTest {
    private static class TestManager<URI, V> implements PersistenceManager<URI, V>{

        private Map<URI, V> mem;

        private TestManager() {
            mem = new HashMap<URI, V>();
        }

        @Override
        public void serialize(URI uri, V val) {
            mem.put(uri, val);
        }

        @Override
        public V deserialize(URI uri) {
            return mem.get(uri);
        }

        @Override
        public boolean delete(URI uri)  {
            return mem.remove(uri) != null;
        }
    }

    private BTree<Integer, String> testTree;

    public BTreeImplTest() {
        testTree = new BTreeImpl<>();
        TestManager<Integer, String> manager = new TestManager<>();
        testTree.setPersistenceManager(manager);
    }

    @Test
    public void testPutandGet() {
        testTree = new BTreeImpl<>();
        TestManager<Integer, String> manager = new TestManager<>();
        testTree.setPersistenceManager(manager);
        for (int i=0; i < 100; i++) {
            testTree.put(i, "" + i);
        }
        assertNotNull(testTree.get(0));
        for (int i=0; i < 100; i++) {
            assertEquals("" + i, testTree.get(i));
        }
    }

    @Test
    public void testReturnsNull() {
        testTree = new BTreeImpl<>();
        TestManager<Integer, String> manager = new TestManager<>();
        testTree.setPersistenceManager(manager);
        assertEquals(null, testTree.put(1, "First"));
        assertNull(testTree.get(4));
        assertEquals("First", testTree.put(1, null));
        assertNull(testTree.get(3));
        assertEquals(null, testTree.put(2, "Second"));
    }

    @Test
    public void testPut() {
        testTree = new BTreeImpl<>();
        TestManager<Integer, String> manager = new TestManager<>();
        testTree.setPersistenceManager(manager);
        testTree.put(1, "Uno");
        testTree.put(2, "Dos");
        assertEquals("Uno", testTree.get(1));
        testTree.put(3, "Tries -hahaha");
        assertEquals("Tries -hahaha", testTree.get(3));
    }

    @Test
    public void testDelete() {
        testTree = new BTreeImpl<>();
        TestManager<Integer, String> manager = new TestManager<>();
        testTree.setPersistenceManager(manager);
        testTree.put(3, "Cheese");
        testTree.put(4, "Smoked cheese");
        assertEquals("Cheese", testTree.get(3));
        testTree.put(3, null);
        assertNull(testTree.get(3));
    }

    @Test
    public void testPutReplace() {
        testTree = new BTreeImpl<>();
        TestManager<Integer, String> manager = new TestManager<>();
        testTree.setPersistenceManager(manager);
        testTree.put(1, "Uno");
        testTree.put(2, "Dos");
        assertEquals("Uno", testTree.put(1, "Alt"));
    }

    // tests for moveToDisk
    // test that move to disk actually gets there
    @Test
    public void moveToDiskGetsItThere() throws Exception {
        testTree = new BTreeImpl<>();
        TestManager<Integer, String> manager = new TestManager<>();
        testTree.setPersistenceManager(manager);
        testTree.put(3, "Cheese");
        testTree.put(4, "Smoked cheese");
        assertNull(manager.deserialize(3));
        testTree.moveToDisk(3);
        assertEquals("Cheese", manager.deserialize(3));
    }

    // I need to make sure I can move to disk and get still works
    @Test
    public void moveToDiskGetBringsBack() throws Exception {
        testTree = new BTreeImpl<>();
        TestManager<Integer, String> manager = new TestManager<>();
        testTree.setPersistenceManager(manager);
        testTree.put(3, "Cheese");
        testTree.put(4, "Smoked cheese");
        testTree.setPersistenceManager(manager);
        assertNull(manager.deserialize(3));
        testTree.moveToDisk(3);
        assertEquals("Cheese", manager.deserialize(3));
        assertEquals("Cheese", testTree.get(3));
    }

    // When I get from the disk, it is no longer in the disk (using my own persistence manager)
    @Test
    public void moveToDiskGetNoLongerOnDisk() throws Exception {
        testTree = new BTreeImpl<>();
        TestManager<Integer, String> manager = new TestManager<>();
        testTree.setPersistenceManager(manager);
        testTree.put(3, "Cheese");
        testTree.put(4, "Smoked cheese");
        testTree.setPersistenceManager(manager);
        assertNull(manager.deserialize(3));
        testTree.moveToDisk(3);
        assertEquals("Cheese", manager.deserialize(3));
        assertEquals("Cheese", testTree.get(3));
        assertNull(manager.deserialize(3));
    }

    // When I put to something in the disk, it is no longer in the disk
    @Test
    public void moveToDiskPutBringsBack() throws Exception {
        testTree = new BTreeImpl<>();
        TestManager<Integer, String> manager = new TestManager<>();
        testTree.setPersistenceManager(manager);
        testTree.put(3, "Cheese");
        testTree.put(4, "Smoked cheese");
        testTree.setPersistenceManager(manager);
        assertNull(manager.deserialize(3));
        testTree.moveToDisk(3);
        assertEquals("Cheese", manager.deserialize(3));
        testTree.put(3, "Cheddar");
        assertNull(manager.deserialize(3));
    }

    // testing that if move to disk and delete, get works correctly
    @Test
    public void moveToDiskDeleteWorks() throws Exception {
        testTree = new BTreeImpl<>();
        TestManager<Integer, String> manager = new TestManager<>();
        testTree.setPersistenceManager(manager);
        testTree.put(3, "Cheese");
        testTree.put(4, "Smoked cheese");
        testTree.setPersistenceManager(manager);
        assertNull(manager.deserialize(3));
        testTree.moveToDisk(3);
        assertEquals("Cheese", manager.deserialize(3));
        testTree.put(3, null);
        assertNull(manager.deserialize(3));
        assertNull(testTree.get(3));
    }

}