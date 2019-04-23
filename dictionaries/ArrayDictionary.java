package datastructures.concrete.dictionaries;

import datastructures.concrete.KVPair;
import datastructures.interfaces.IDictionary;
import misc.exceptions.NoSuchKeyException;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;


public class ArrayDictionary<K, V> implements IDictionary<K, V> {
    private Pair<K, V>[] pairs;
    //additional fields
    private int currSize; // the current size of the array
    private int initSize; // initial starting size
    // You're encouraged to add extra fields (and helper methods) though!
    
    //if the user has a size in mind before initialization
    public ArrayDictionary(int initSize) {
    	this.pairs = makeArrayOfPairs(initSize);
    	this.currSize = 0;
    	this.initSize = initSize;
    }
    
    //default initialization
    public ArrayDictionary() {
    	this.pairs = makeArrayOfPairs(4);
        this.initSize = 4;
        this.currSize = 0;
    }

    /**
     * This method will return a new, empty array of the given size
     * that can contain Pair<K, V> objects.
     *
     * Note that each element in the array will initially be null.
     */
    @SuppressWarnings("unchecked")
    private Pair<K, V>[] makeArrayOfPairs(int arraySize) {
        // It turns out that creating arrays of generic objects in Java
        // is complicated due to something known as 'type erasure'.
        //
        // We've given you this helper method to help simplify this part of
        // your assignment. Use this helper method as appropriate when
        // implementing the rest of this class.
        //
        // You are not required to understand how this method works, what
        // type erasure is, or how arrays and generics interact. Do not
        // modify this method in any way.
        return (Pair<K, V>[]) (new Pair[arraySize]);
    }

    @Override
    public V get(K key) {
        if (currSize != 0) {        	      
        	for (int i = 0; i < currSize; i++) {
        		if (Objects.equals(pairs[i].key, key)) {
        			return pairs[i].value;
        		}
        	}
        }
        throw new NoSuchKeyException(); //no key is found at the end
    }

    @Override
    public void put(K key, V value) {
        boolean contain = false;
        if (currSize == initSize) { //if the array is full
        	Pair<K, V>[] newPair = makeArrayOfPairs(initSize*2); //
        	initSize = initSize * 2; //
        	for (int i = 0; i < currSize; i++) {
        		newPair[i] = new Pair<K, V>(pairs[i].key, pairs[i].value);
        	}
        	pairs = newPair;
        }
        for (int k = 0; k < currSize; k++) {
       		if (Objects.equals(pairs[k].key, key)) {
       			pairs[k].value = value;
       			contain = true;
       			break;
       		}
       	}
       	if (!contain){
       	    pairs[currSize] = new Pair<K, V>(key, value);
            this.currSize++;
       	}
    }

    @Override
    public V remove(K key) {
    	if (!this.containsKey(key)) {
    		throw new NoSuchKeyException();
    	}
    	V result = this.get(key);
    	for (int i = 0; i < currSize; i++) { // find the index
    		if (Objects.equals(pairs[i].key, key)) {
    			if (currSize==1 || i==this.currSize-1) {
    				pairs[i] = null; // optional
    			} else {
    				pairs[i] = new Pair<K, V>(pairs[currSize-1]); // replaced it with the last key 
    				pairs[currSize-1] = null;						
    			}
    			this.currSize--;
    			break;
    		}
    	}
    	return result;
    }

    @Override
    public boolean containsKey(K key) {
        for (int i = 0; i < currSize; i++) {
        	if (Objects.equals(pairs[i].key, key)) {
        		return true;
        	}
        }
        return false;
    }
    
    @Override
    public int size() {
    	return this.currSize;
    }
    
    

    public static class Pair<K, V> {
        public K key;
        public V value;

        // You may add constructors and methods to this class as necessary.
        public Pair(K key, V value) {
            this.key = key;
            this.value = value;
        }
        
        public Pair(Pair<K, V> original){
            this.key = original.key;
            this.value = original.value;
        }

        @Override
        public String toString() {
            return this.key + "=" + this.value;
        }
    }
   
    private static class ArrayDictionaryIterator<K, V> implements Iterator<KVPair<K, V>> {
    	Pair<K, V>[] pair;
    	private int index;
    	private int size;
    	
        public ArrayDictionaryIterator(Pair<K, V>[] pairs, int currSize) {
        	size = currSize;
        	this.pair = pairs;
        }
        
        public boolean hasNext() {
        	if (index < size) {
        		return true;
        	} 
        	return false;
        }
        
        // return the pair at that index and then move the index forward
        public KVPair<K, V> next() {
        	if (!this.hasNext()){
                throw new NoSuchElementException();
            }
        	KVPair<K, V> result = new KVPair<K, V>(pair[index].key, pair[index].value);
        	index++;					
        	return result;        	
        }
    }
    
    
    @Override
    public Iterator<KVPair<K, V>> iterator() {
    	return new ArrayDictionaryIterator<K, V>(this.pairs, this.currSize);
    }
}
