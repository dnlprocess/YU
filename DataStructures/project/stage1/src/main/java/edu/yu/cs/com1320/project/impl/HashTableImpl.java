package edu.yu.cs.com1320.project.impl;

import edu.yu.cs.com1320.project.HashTable;

public class HashTableImpl<Key,Value> implements HashTable<Key,Value>{

    private int size = 5;

    private class Pair<Key, Value>{
        final Key key;
        Value value;
        Pair(Key k, Value v){  //constructor for Entry
            this.key = k;
            this.value = v;
        }
    }
    
    private Pair<Key,Value>[][] hashTable;

    public HashTableImpl() {
        this.hashTable = new Pair[this.size][1];
    }

    /**
     * @param k the key whose value should be returned
     * @return the value that is stored in the HashTable for k, or null if there is no such key in the table
     */
    public Value get(Key k) {
        int index = this.hashIndex(k);
        for(int i=0; i < this.hashTable[index].length; i++) {
            if (this.hashTable[index][i].key.equals(k)) {
                return (Value) hashTable[index][i].value;
            }
        }
        return null;
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
        Pair<Key, Value> newPair = new Pair<Key, Value>(k, v);
        int index = this.hashIndex(k);
        Value pastValue = null;
        
        int i;
        for (i=0; i < this.hashTable[index].length && this.hashTable[index][i] != null; i++) {
            if (this.hashTable[index][i].key.equals(k)) {
                pastValue = this.hashTable[index][i].value;
                this.hashTable[index][i] = newPair;
                return pastValue;
            }
        }

        if (i < this.hashTable[index].length) {
            doubleIndex(index);
        }
        this.hashTable[index][i+1] = newPair;
        return pastValue;
    }

    private int hashIndex(Key key){
        return (key.hashCode() & 0x7fffffff) % this.size;
        }

    private void doubleIndex(int index) {
        int length = 2*this.hashTable[index].length;
        Pair<Key, Value>[] arr = new Pair[length];
        for (int i=0; i< this.hashTable[index].length; i++) {
            arr[i] = this.hashTable[index][i];
        }
        this.hashTable[index] = arr;
    }
}
