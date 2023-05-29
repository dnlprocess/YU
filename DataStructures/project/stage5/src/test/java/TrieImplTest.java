import edu.yu.cs.com1320.project.impl.TrieImpl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Comparator;
import java.util.List;
import java.util.Set;

public class TrieImplTest {

    @Test
    public void testPutAndGet() {
        TrieImpl<String> trie = new TrieImpl<>();
        trie.put("hello", "world");
        trie.put("hello", "tomorrow");
        trie.put("hell", "fire");
        trie.put("heller", "danger");
        trie.put("hi", "there");
        trie.put("hola", "there");

        List<String> matches = trie.getAllSorted("hell", Comparator.naturalOrder());
        assertEquals(1, matches.size());
        matches = trie.getAllWithPrefixSorted("h", Comparator.naturalOrder());
        assertEquals("there", matches.get(0));
        assertEquals("fire", matches.get(2));
        assertEquals("tomorrow", matches.get(1));
        assertEquals("world", matches.get(0));

        matches = trie.getAllSorted("hello", Comparator.naturalOrder());
        assertEquals(2, matches.size());
        assertEquals("world", matches.get(0));

        matches = trie.getAllSorted("heller", Comparator.naturalOrder());
        assertEquals(1, matches.size());
        assertEquals("danger", matches.get(0));

        matches = trie.getAllSorted("hi", Comparator.naturalOrder());
        assertEquals(1, matches.size());
        assertEquals("there", matches.get(0));
    }

    @Test
    public void testDeleteAllWithPrefix() {
        TrieImpl<String> trie = new TrieImpl<>();
        trie.put("hello", "world");
        trie.put("hell", "fire");
        trie.put("heller", "danger");
        trie.put("hi", "there");
        trie.put("hiya", "mate");
        trie.put("hey", "you");

        Set<String> deletedValues = trie.deleteAllWithPrefix("he");
        assertEquals(4, deletedValues.size());
        assertTrue(deletedValues.contains("world"));
        assertTrue(deletedValues.contains("fire"));
        assertTrue(deletedValues.contains("danger"));
        assertTrue(deletedValues.contains("you"));

        List<String> matches = trie.getAllSorted("hello", Comparator.naturalOrder());
        assertTrue(matches.isEmpty());

        matches = trie.getAllSorted("hell", Comparator.naturalOrder());
        assertTrue(matches.isEmpty());

        matches = trie.getAllSorted("heller", Comparator.naturalOrder());
        assertTrue(matches.isEmpty());

        matches = trie.getAllSorted("hi", Comparator.naturalOrder());
        assertEquals(1, matches.size());
        assertEquals("there", matches.get(0));

        matches = trie.getAllSorted("hiya", Comparator.naturalOrder());
        assertEquals(1, matches.size());
        assertEquals("mate", matches.get(0));

        matches = trie.getAllSorted("hey", Comparator.naturalOrder());
        assertTrue(matches.isEmpty());
    }

    @Test
    public void testDeleteAll() {
        TrieImpl<String> trie = new TrieImpl<>();
        trie.put("hello", "world");
        trie.put("hell", "fire");
        trie.put("heller", "danger");
        trie.put("hi", "there");
        trie.put("hiya", "mate");
        trie.put("hey", "you");

        Set<String> deletedValues = trie.deleteAll("hello");
        assertEquals(1, deletedValues.size());
        assertTrue(deletedValues.contains("world"));

        List<String> matches = trie.getAllSorted("hello", Comparator.naturalOrder());
        assertTrue(matches.isEmpty());

        matches = trie.getAllSorted("hell", Comparator.naturalOrder());
        assertEquals(1, matches.size());
        assertEquals("fire", matches.get(0));

        matches = trie.getAllSorted("heller", Comparator.naturalOrder());
        assertEquals(1, matches.size());
        assertEquals("danger", matches.get(0));

        matches = trie.getAllSorted("hi", Comparator.naturalOrder());
        assertEquals(1, matches.size());
        assertEquals("there", matches.get(0));

        matches = trie.getAllSorted("hiya", Comparator.naturalOrder());
        assertEquals(1, matches.size());
        assertEquals("mate", matches.get(0));

        matches = trie.getAllSorted("hey", Comparator.naturalOrder());
        assertEquals(1, matches.size());
        assertEquals("you", matches.get(0));

        deletedValues = trie.deleteAll("hell");
        assertEquals(2, deletedValues.size());
        assertTrue(deletedValues.contains("fire"));
        assertTrue(deletedValues.contains("danger"));

        matches = trie.getAllSorted("hell", Comparator.naturalOrder());
        assertTrue(matches.isEmpty());

        matches = trie.getAllSorted("heller", Comparator.naturalOrder());
        assertTrue(matches.isEmpty());

        matches = trie.getAllSorted("hi", Comparator.naturalOrder());
        assertEquals(1, matches.size());
        assertEquals("there", matches.get(0));

        matches = trie.getAllSorted("hiya", Comparator.naturalOrder());
        assertEquals(1, matches.size());
        assertEquals("mate", matches.get(0));

        matches = trie.getAllSorted("hey", Comparator.naturalOrder());
        assertEquals(1, matches.size());
        assertEquals("you", matches.get(0));

        deletedValues = trie.deleteAll("hey");
        assertEquals(1, deletedValues.size());
        assertTrue(deletedValues.contains("you"));

        matches = trie.getAllSorted("hell", Comparator.naturalOrder());
        assertTrue(matches.isEmpty());

        matches = trie.getAllSorted("heller", Comparator.naturalOrder());
        assertTrue(matches.isEmpty());

        matches = trie.getAllSorted("hi", Comparator.naturalOrder());
        assertEquals(1, matches.size());
        assertEquals("there", matches.get(0));

        matches = trie.getAllSorted("hiya", Comparator.naturalOrder());
        assertEquals(1, matches.size());
        assertEquals("mate", matches.get(0));

        matches = trie.getAllSorted("hey", Comparator.naturalOrder());
        assertTrue(matches.isEmpty());
    }

}