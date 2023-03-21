
import static org.junit.Assert.*;

import org.junit.Test;

import edu.yu.cs.com1320.project.HashTable;
import edu.yu.cs.com1320.project.impl.HashTableImpl;


public class HashTableImplTest {
    private HashTable<String, String> hashTable;


    @Test
    public void testPutAndGet() {
        this.hashTable = new HashTableImpl<>();
        this.hashTable.put("key1", "value1");
        this.hashTable.put("key2", "value2");
        assertEquals("value1", this.hashTable.get("key1"));
        assertEquals("value2", this.hashTable.get("key2"));
    }

    @Test
    public void testPutAndGetWithNullValue() {
        this.hashTable = new HashTableImpl<>();
        this.hashTable.put("key1", "value1");
        this.hashTable.put("key2", null);
        assertEquals("value1", this.hashTable.get("key1"));
        assertNull(this.hashTable.get("key2"));
    }


    @Test
    public void testContainsKey() {
        this.hashTable = new HashTableImpl<>();
        this.hashTable.put("key1", "value1");
        assertTrue(this.hashTable.containsKey("key1"));
        assertFalse(this.hashTable.containsKey("key2"));
    }

    @Test
    public void testDelete() {
        this.hashTable = new HashTableImpl<>();
        this.hashTable.put("key1", "value1");
        assertEquals("value1", this.hashTable.put("key1", null));
        assertNull(this.hashTable.get("key1"));
    }

    @Test
    public void testDeleteNonExistingKey() {
        this.hashTable = new HashTableImpl<>();
        assertNull(this.hashTable.put("key1", null));
    }

    @Test
    public void testPutAndGetWithCollisions() {
        this.hashTable = new HashTableImpl<>();
        this.hashTable.put("key1", "value1");
        this.hashTable.put("key6", "value6");
        assertEquals("value1", this.hashTable.get("key1"));
        assertEquals("value6", this.hashTable.get("key6"));
    }
}
