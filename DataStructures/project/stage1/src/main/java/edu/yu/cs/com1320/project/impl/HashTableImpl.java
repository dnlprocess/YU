package edu.yu.cs.com1320.project.impl;

import edu.yu.cs.com1320.project.HashTable;

public class HashTableImpl<Key,Value> implements HashTable<Key,Value>{

    private int size = 5;

    class Pair<Key, Value>{
        final Key key;
        Value value;
        Pair(Key k, Value v){  //constructor for Entry
            this.key = k;
            this.value = v;
        }
    }
    
    private Pair<?,?>[][] hashTable;

    public HashTableImpl() {
        this.hashTable = new Pair[this.size][];
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

    }

    /**
     * @param k the key at which to store the value
     * @param v the value to store.
     * To delete an entry, put a null value.
     * @return if the key was already present in the HashTable, return the previous value stored for the key. If the key was not already present, return null.
     */
    public Value put(Key k, Value v) {
        Value pastValue = null;
        if (this.get(k) != null) {
            // = k
            
        }
        this.get(k);
        return pastValue;
    }

    private int hashIndex(Key key){
        return (key.hashCode() & 0x7fffffff) % this.size;
        }
}
