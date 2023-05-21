package edu.yu.cs.com1320.project.impl;

import java.util.Arrays;

import edu.yu.cs.com1320.project.BTree;
import edu.yu.cs.com1320.project.stage5.PersistenceManager;

public class BTreeImpl<Key extends Comparable<Key>, Value> implements BTree<Key,Value> {

    //max children per B-tree node = MAX-1 (must be an even number and greater than 2)
    private static final int MAX = 6;
    private Node root; //root of the B-tree
    private Node leftMostExternalNode;
    private int height; //height of the B-tree
    private int n; //number of key-value pairs in the B-tree
    private PersistenceManager<Key,Value> persistenceManager;

    //B-tree node data type
    private static final class Node <Key extends Comparable<Key>, Value>
    {
        private int entryCount; // number of entries
        private Entry[] entries = new Entry[BTreeImpl.MAX]; // the array of children
        private Node next;
        private Node previous;

        // create a node with k entries
        private Node(int k)
        {
            this.entryCount = k;
        }

        private void setNext(Node next)
        {
            this.next = next;
        }
        private Node getNext()
        {
            return this.next;
        }
        private void setPrevious(Node previous)
        {
            this.previous = previous;
        }
        private Node getPrevious()
        {
            return this.previous;
        }

        private Entry[] getEntries()
        {
            return Arrays.copyOf(this.entries, this.entryCount);
        }

    }

    //internal nodes: only use key and child
    //external nodes: only use key and value
    private static class Entry
    {
        private Comparable key;
        private Object val; //needs to either be a value or a pointer to a value
        private Node child;
        private boolean serialized;

        public Entry(Comparable key, Object val, Node child)
        {
            this.key = key;
            this.val = val;
            this.child = child;
            this.serialized = false;
        }
        public Object getValue()
        {
            return this.val;
        }
        public Comparable getKey()
        {
            return this.key;
        }
        public boolean getSerialization() {
            return this.serialized;
        }
        public void setSerialization(boolean serialized) {
            this.serialized = serialized;
        }
    }

    public BTreeImpl()
    {
        this.root = new Node(0);
        this.leftMostExternalNode = this.root;
    }

    @Override
    public Value get(Key k) {
        if (k == null)
        {
            throw new IllegalArgumentException("argument to get() is null");
        }
        Entry entry = this.get(this.root, k, this.height);
        if(entry != null)
        {
            return (Value)entry.val;
        }
        return null;
    }

    private Entry get(Node currentNode, Key key, int height)
    {
        Entry[] entries = currentNode.entries;

        //current node is external (i.e. height == 0)
        if (height == 0)
        {
            for (int j = 0; j < currentNode.entryCount; j++)
            {
                if(isEqual(key, entries[j].key))
                {
                    //found desired key. Return its value
                    return entries[j];
                }
            }
            //didn't find the key
            return null;
        }

        //current node is internal (height > 0)
        else
        {
            for (int j = 0; j < currentNode.entryCount; j++)
            {
                //if (we are at the last key in this node OR the key we
                //are looking for is less than the next key, i.e. the
                //desired key must be in the subtree below the current entry),
                //then recurse into the current entry’s child
                if (j + 1 == currentNode.entryCount || less(key, entries[j + 1].key))
                {
                    return this.get(entries[j].child, key, height - 1);
                }
            }
            //didn't find the key
            return null;
        }
    }

    @Override
    public Object put(Comparable k, Object v) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'put'");
    }

    @Override
    public void moveToDisk(Comparable k) throws Exception {
        //call get
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'moveToDisk'");
    }

    @Override
    public void setPersistenceManager(PersistenceManager<Key,Value> pm) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setPersistenceManager'");
    }



    private static boolean less(Comparable<Key> k1, Key k2)
    {
        return k1.compareTo(k2) < 0;
    }
    
}
