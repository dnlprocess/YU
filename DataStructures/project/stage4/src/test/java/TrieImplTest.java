import edu.yu.cs.com1320.project.impl.TrieImpl;

import org.junit.Test;
import org.junit.Assert;

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

        List<String> matches = trie.getAllSorted("hell", Comparator.naturalOrder());
        Assert.assertEquals(1, matches.size());
        matches = trie.getAllWithPrefixSorted("hell", Comparator.naturalOrder());
        Assert.assertEquals("danger", matches.get(3));
        Assert.assertEquals("fire", matches.get(2));
        Assert.assertEquals("tomorrow", matches.get(1));
        Assert.assertEquals("world", matches.get(0));

        matches = trie.getAllSorted("hello", Comparator.naturalOrder());
        Assert.assertEquals(2, matches.size());
        Assert.assertEquals("world", matches.get(0));

        matches = trie.getAllSorted("heller", Comparator.naturalOrder());
        Assert.assertEquals(1, matches.size());
        Assert.assertEquals("danger", matches.get(0));

        matches = trie.getAllSorted("hi", Comparator.naturalOrder());
        Assert.assertEquals(1, matches.size());
        Assert.assertEquals("there", matches.get(0));
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
        Assert.assertEquals(4, deletedValues.size());
        Assert.assertTrue(deletedValues.contains("world"));
        Assert.assertTrue(deletedValues.contains("fire"));
        Assert.assertTrue(deletedValues.contains("danger"));
        Assert.assertTrue(deletedValues.contains("you"));

        List<String> matches = trie.getAllSorted("hello", Comparator.naturalOrder());
        Assert.assertTrue(matches.isEmpty());

        matches = trie.getAllSorted("hell", Comparator.naturalOrder());
        Assert.assertTrue(matches.isEmpty());

        matches = trie.getAllSorted("heller", Comparator.naturalOrder());
        Assert.assertTrue(matches.isEmpty());

        matches = trie.getAllSorted("hi", Comparator.naturalOrder());
        Assert.assertEquals(1, matches.size());
        Assert.assertEquals("there", matches.get(0));

        matches = trie.getAllSorted("hiya", Comparator.naturalOrder());
        Assert.assertEquals(1, matches.size());
        Assert.assertEquals("mate", matches.get(0));

        matches = trie.getAllSorted("hey", Comparator.naturalOrder());
        Assert.assertTrue(matches.isEmpty());
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
        Assert.assertEquals(1, deletedValues.size());
        Assert.assertTrue(deletedValues.contains("world"));

        List<String> matches = trie.getAllSorted("hello", Comparator.naturalOrder());
        Assert.assertTrue(matches.isEmpty());

        matches = trie.getAllSorted("hell", Comparator.naturalOrder());
        Assert.assertEquals(1, matches.size());
        Assert.assertEquals("fire", matches.get(0));

        matches = trie.getAllSorted("heller", Comparator.naturalOrder());
        Assert.assertEquals(1, matches.size());
        Assert.assertEquals("danger", matches.get(0));

        matches = trie.getAllSorted("hi", Comparator.naturalOrder());
        Assert.assertEquals(1, matches.size());
        Assert.assertEquals("there", matches.get(0));

        matches = trie.getAllSorted("hiya", Comparator.naturalOrder());
        Assert.assertEquals(1, matches.size());
        Assert.assertEquals("mate", matches.get(0));

        matches = trie.getAllSorted("hey", Comparator.naturalOrder());
        Assert.assertEquals(1, matches.size());
        Assert.assertEquals("you", matches.get(0));

        deletedValues = trie.deleteAll("hell");
        Assert.assertEquals(2, deletedValues.size());
        Assert.assertTrue(deletedValues.contains("fire"));
        Assert.assertTrue(deletedValues.contains("danger"));

        matches = trie.getAllSorted("hell", Comparator.naturalOrder());
        Assert.assertTrue(matches.isEmpty());

        matches = trie.getAllSorted("heller", Comparator.naturalOrder());
        Assert.assertTrue(matches.isEmpty());

        matches = trie.getAllSorted("hi", Comparator.naturalOrder());
        Assert.assertEquals(1, matches.size());
        Assert.assertEquals("there", matches.get(0));

        matches = trie.getAllSorted("hiya", Comparator.naturalOrder());
        Assert.assertEquals(1, matches.size());
        Assert.assertEquals("mate", matches.get(0));

        matches = trie.getAllSorted("hey", Comparator.naturalOrder());
        Assert.assertEquals(1, matches.size());
        Assert.assertEquals("you", matches.get(0));

        deletedValues = trie.deleteAll("hey");
        Assert.assertEquals(1, deletedValues.size());
        Assert.assertTrue(deletedValues.contains("you"));

        matches = trie.getAllSorted("hell", Comparator.naturalOrder());
        Assert.assertTrue(matches.isEmpty());

        matches = trie.getAllSorted("heller", Comparator.naturalOrder());
        Assert.assertTrue(matches.isEmpty());

        matches = trie.getAllSorted("hi", Comparator.naturalOrder());
        Assert.assertEquals(1, matches.size());
        Assert.assertEquals("there", matches.get(0));

        matches = trie.getAllSorted("hiya", Comparator.naturalOrder());
        Assert.assertEquals(1, matches.size());
        Assert.assertEquals("mate", matches.get(0));

        matches = trie.getAllSorted("hey", Comparator.naturalOrder());
        Assert.assertTrue(matches.isEmpty());
    }

}