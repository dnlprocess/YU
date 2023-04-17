import static org.junit.Assert.*;

import org.junit.Test;

import edu.yu.cs.com1320.project.HashTable;
import edu.yu.cs.com1320.project.impl.HashTableImpl;


public class HashTableImplTest {
    private HashTableImpl<String, String> hashTable;


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
    public void testCollisions() {
        this.hashTable = new HashTableImpl<>();
        this.hashTable.put("key1", "value1");
        this.hashTable.put("key6", "value6");
        assertEquals("value1", this.hashTable.get("key1"));
        assertEquals("value6", this.hashTable.get("key6"));
    }
/*
    @Test
    public void testDoublingArray() {
        this.hashTable = new HashTableImpl<>();
        this.hashTable.put("key1", "value1");
        this.hashTable.put("key2", "value2");
        this.hashTable.put("key2", "value1");
        this.hashTable.put("key4", "value6");
        this.hashTable.put("key5", "value1");
        this.hashTable.put("key6", "value6");
        this.hashTable.put("key7", "value1");
        this.hashTable.put("key8", "value6");
        this.hashTable.put("key9", "value1");
        this.hashTable.put("key10", "value6");
        this.hashTable.put("key11", "value1");
        this.hashTable.put("key12", "value6");
        this.hashTable.put("key13", "value1");
        this.hashTable.put("key14", "value6");
        this.hashTable.put("key15", "value1");
        this.hashTable.put("key16", "value6");
        this.hashTable.put("key17", "value1");
        this.hashTable.put("key18", "value6");
        this.hashTable.put("key19", "value1");
        this.hashTable.put("key20", "value6");
        this.hashTable.put("key21", "value6");
        this.hashTable.put("key22", "value6");
        this.hashTable.put("key23", "value6");
        this.hashTable.put("key24", "value6");
        this.hashTable.put("key25", "value6");
        this.hashTable.put("key26", "value26");
        assertEquals(Integer.valueOf(10), Integer.valueOf(this.hashTable.size()));
    }
    */
}
