package edu.yu.cs.com1320.project.impl;

import edu.yu.cs.com1320.project.HashTable;

public class HashTableImpl<Key,Value> implements HashTable<Key,Value>{

    //public int pairs;
    private int pairs;

    private class Entry<Key, Value> {
        Pair head;

        class Pair{
            Key key;
            Value value;
            Pair next;

            Pair(Key k, Value v, Pair next){
                if (v == null) {
                    this.key = null;
                    this.value = null;
                }
                else {
                    this.key = k;
                    this.value = v;
                }
                this.next = next;
            }
        }

        Key getKey() {
            return this.head.key;
        }

        Value get(Key key) {
            for (Pair pair = this.head; pair != null; pair = pair.next) {
                if (pair.key.equals(key)) {
                    return pair.value;
                }
            }
            return null;
        }

        Value put(Key key, Value value) {
            Value pastValue = null;
            for (Pair current = this.head; current != null; current = current.next) {
                if (current.key.equals(key)) {
                    pastValue = current.value;
                    current.value = value;
                    return pastValue;
                }
            }
            this.head = new Pair(key, value, this.head);
            return pastValue;
        }

        Value delete(Key key) {
            return deleteHelper(this.head, key);
        }

        private Value deleteHelper(Pair current, Key key) {
            if (current == null) {
                return null;
            }

            if (current.key.equals(key)) {
                Value pastValue = current.value;
                this.head = current.next;
                return pastValue;
            }

            Value pastValue = deleteHelper(current.next, key);
            if (pastValue != null) {
                current.next = current.next.next;
            }
            return pastValue;
        }
    }

    private Entry<Key, Value>[] hashTable;

    @SuppressWarnings("unchecked")
    public HashTableImpl() {
        this.hashTable = (Entry<Key, Value>[]) new Entry[5];
        for (int i=0; i < this.hashTable.length; i++) {
            this.hashTable[i] = new Entry<Key, Value>();
        }
    }

    /**
     * @param k the key whose value should be returned
     * @return the value that is stored in the HashTable for k, or null if there is no such key in the table
     */
    public Value get(Key k) {
        int index = this.hashIndex(k);

        return (Value) this.hashTable[index].get(k);
    }

    /**
     * @param key the key whose presence in the hashtabe we are inquiring about
     * @return true if the given key is present in the hashtable as a key, false if not
     * @throws NullPointerException if the specified key is null
     */
    public boolean containsKey(Key key){
        if (key == null) {
            throw new NullPointerException();
        }
        return (this.get(key) != null) ? true : false;
    }

    /**
     * @param k the key at which to store the value
     * @param v the value to store.
     * To delete an entry, put a null value.
     * @return if the key was already present in the HashTable, return the previous value stored for the key.
     * If the key was not already present, return null.
     */
    public Value put(Key k, Value v) {
        int index = this.hashIndex(k);

        if (v == null) {
            this.pairs--;
            return this.hashTable[index].delete(k);
        }
        this.pairs++;
        if (this.pairs/this.hashTable.length >= 4) {
            resize();
        }
        return this.hashTable[index].put(k,v);
    }

    private int hashIndex(Key key){
        return (key.hashCode() & 0x7fffffff) % this.hashTable.length;
    }

    @SuppressWarnings("unchecked")
    private void resize() {
        Entry<Key, Value>[] tempHashTable = this.hashTable;
        
        this.hashTable = (Entry<Key, Value>[]) new Entry[2*this.hashTable.length];
        for (int i=0; i < this.hashTable.length; i++) {
            this.hashTable[i] = new Entry<Key, Value>();
        }

        for (int i=0; i < tempHashTable.length;i++) {
            Entry<Key, Value> current = tempHashTable[i];
            while(current.head != null) {
                this.hashTable[hashIndex(current.getKey())].put(current.getKey(), current.get(current.getKey()));
                current.delete(current.getKey());
            }
        }
    }

    //public int size() {
    //    return this.hashTable.length;
    //}
}
