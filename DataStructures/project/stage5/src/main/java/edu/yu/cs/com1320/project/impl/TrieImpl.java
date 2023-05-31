package edu.yu.cs.com1320.project.impl;

import edu.yu.cs.com1320.project.Trie;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

public class TrieImpl<Value> implements Trie<Value>{

    private final int alphabetSize = 256; // extended ASCII
    private Node<Value> root; // root of trie

    public TrieImpl() {
        root = new Node<>();
    }

    @SuppressWarnings("unchecked")
    private class Node<Value> {
        private List<Value> vals;
        private Node<Value>[] links;

        public Node(){
            this.vals = new ArrayList<>();
            this.links = (Node<Value>[]) new Node[alphabetSize];
        }
    }


    /**
     * add the given value at the given key
     * @param key
     * @param val
     */
    public void put(String key, Value val) {
        if (key == null) {
            throw new IllegalArgumentException();
        }
        if (val == null) {
            return;
        }
        this.root = put(this.root, key, val, 0);
    
    }

    
    private Node<Value> put(Node<Value> node, String key, Value val, int depth) {
        if (node == null) {
            node = new Node<Value>();
        }

        if (depth == key.length()) {
            node.vals.add(val);
            return node;
        }

        //proceed to the next node in the chain of nodes that
        //forms the desired key
        char c = key.charAt(depth);
        node.links[c] = this.put(node.links[c], key, val, depth + 1);
        return node;
    }


    /**
     * get all exact matches for the given key, sorted in descending order.
     * Search is CASE SENSITIVE.
     * @param key
     * @param comparator used to sort  values
     * @return a List of matching Values, in descending order
     */
    public List<Value> getAllSorted(String key, Comparator<Value> comparator) {
        if (comparator == null || key == null) {
            throw new IllegalArgumentException();
        }

        List<Value> matches = new ArrayList<Value>();
        Node<Value> x = this.get(this.root, key, 0);
        if (x == null) {
            return matches;
        }
        matches = x.vals;
        Collections.sort(matches, comparator);
        return matches;
    }

    private Node<Value> get(Node<Value> node, String key, int depth) {
        if (node == null) {
            return null;
        }
        
        if (depth == key.length()) {
            return node;
        }

        char c = key.charAt(depth);
        return this.get(node.links[c], key, depth + 1);
    }

    /**
     * get all matches which contain a String with the given prefix, sorted in descending order.
     * For example, if the key is "Too", you would return any value that contains "Tool", "Too", "Tooth", "Toodle", etc.
     * Search is CASE SENSITIVE.
     * @param prefix
     * @param comparator used to sort values
     * @return a List of all matching Values containing the given prefix, in descending order
     */
    public List<Value> getAllWithPrefixSorted(String prefix, Comparator<Value> comparator) {
        if (comparator == null || prefix == null) {
            throw new IllegalArgumentException();
        }
        
        List<Value> matches = new ArrayList<Value>();
        Node<Value> x = this.get(this.root, prefix, 0);
        if (x == null) {
            return matches;
        }

        getAllValues(x, prefix, matches);
        Collections.sort(matches, comparator);
        return matches;
    }

    private void getAllValues(Node<Value> node, String prefix, List<Value> matches) {
        if (node == null) {
            return;
        }

        for (Value val : node.vals) {
            matches.add(val);
        }
        
        for (char c = 0; c < alphabetSize; c++) {
            prefix += c;
            getAllValues(node.links[c], prefix, matches);
            prefix = prefix.substring(0, prefix.length()-1);
        }
    }

    /**
     * Delete the subtree rooted at the last character of the prefix.
     * Search is CASE SENSITIVE.
     * @param prefix
     * @return a Set of all Values that were deleted.
     */
    public Set<Value> deleteAllWithPrefix(String prefix) {
        if (prefix == null) {
            throw new IllegalArgumentException();
        }
        Set<Value> deletedValues = new HashSet<Value>();

        Node<Value> node = get(root, prefix, 0);

        if (node != null) {
            deleteAllValues(node, deletedValues);
        }
        
        clean();

        return deletedValues;
    }

    private void deleteAllValues(Node<Value> node, Set<Value> deletedValues) {
        for (Value val : node.vals) {
            deletedValues.add(val);
        }
        node.vals.clear();
    
        for (int c = 0; c < alphabetSize; c++) {
            if (node.links[c] != null) {
                deleteAllValues(node.links[c], deletedValues);
                node.links[c] = null;
            }
        }
    }


    /**
     * Delete all values from the node of the given key (do not remove the values from other nodes in the Trie)
     * @param key
     * @return a Set of all Values that were deleted.
     */
    public Set<Value> deleteAll(String key) {
        if (key == null) {
            throw new IllegalArgumentException();
        }

        Set<Value> deletedValues = new HashSet<Value>();
    
        Node<Value> node = get(root, key, 0);
        if (node != null) {
            deleteAllValues(node, deletedValues);
        }
    
        clean();

        return deletedValues;
    }


    /**
     * Remove the given value from the node of the given key (do not remove the value from other nodes in the Trie)
     * @param key
     * @param val
     * @return the value which was deleted. If the key did not contain the given value, return null.
     */
    public Value delete(String key, Value val) {
        if (key == null || val == null) {
            throw new IllegalArgumentException();
        }

        Node<Value> node = get(root, key, 0);

        if (node == null || !node.vals.contains(val)) {
            return null;
        }

        node.vals.remove(val);

        clean();

        return val;
    }

    private void clean() {
        clean(root);
    }
    
    private boolean clean(Node<Value> node) {
        if (node == null) {
            return false;
        }
    
        boolean hasValue = !node.vals.isEmpty();
    
        for (char c = 0; c < alphabetSize; c++) {
            Node<Value> child = node.links[c];
    
            if (clean(child)) {
                node.links[c] = child;
                hasValue = true;
            } else {
                node.links[c] = null;
            }
        }
    
        return hasValue;
    }
}