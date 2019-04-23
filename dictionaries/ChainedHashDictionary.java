package datastructures.concrete.dictionaries;

import datastructures.concrete.KVPair;
import datastructures.interfaces.IDictionary;
import misc.exceptions.NoSuchKeyException;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * See the spec and IDictionary for more details on what each method should do
 */
public class ChainedHashDictionary<K, V> implements IDictionary<K, V> {
    // You may not change or rename this field: we will be inspecting
    // it using our private tests.
    private IDictionary<K, V>[] chains;
    private int hashSize;
    private int keySize;
    
    // You're encouraged to add extra fields (and helper methods) though!

    public ChainedHashDictionary() {
        this.chains = makeArrayOfChains(5);
        this.keySize = 0;
        this.hashSize = chains.length;
    }
    
    public ChainedHashDictionary(IDictionary<K, V>[] chains, int keysize){
        this.chains = chains;
        this.keySize = keysize;
        this.hashSize = chains.length;
    }


    /**
     * This method will return a new, empty array of the given size
     * that can contain IDictionary<K, V> objects.
     *
     * Note that each element in the array will initially be null.
     */
    @SuppressWarnings("unchecked")
    private IDictionary<K, V>[] makeArrayOfChains(int size) {
        // Note: You do not need to modify this method.
        // See ArrayDictionary's makeArrayOfPairs(...) method for
        // more background on why we need this method.
        return (IDictionary<K, V>[]) new IDictionary[size];
    }

    @Override
    //returns the value of the key, throws exception otherwise
    public V get(K key) {
    	IDictionary<K, V> result = this.chains[getHash(key)];
    	if (result == null) {
    	    throw new NoSuchKeyException();
    	}
    	
    	return result.get(key);
    }
    
    
    //returns the hash code , 0 otherwise
    private int getHash(K key) {
    	if (key == null) {
    		return 0;
    	} else {
    		return Math.abs(key.hashCode()) % this.hashSize;
    	}
    }
    
    @Override
    public void put(K key, V value) {
        int hashCode = getHash(key);
        if (chains[hashCode] == null) {
        	chains[hashCode] = new ArrayDictionary<K, V>();
        }
        if (!chains[hashCode].containsKey(key)) {
            keySize++;
        }
        chains[hashCode].put(key, value);
        if (keySize >= hashSize) {
        	reSize();
        }
    }

    private void reSize() {
    	this.hashSize = this.hashSize * 2;
    	IDictionary<K, V> oldPair = new ChainedHashDictionary<K, V>(this.chains, this.keySize);
    	chains = makeArrayOfChains(hashSize);
    	this.keySize = 0;
    	for (KVPair<K, V> kvpair: oldPair) {
    	    this.put(kvpair.getKey(), kvpair.getValue());
    	}
    }

    
    @Override
    public V remove(K key) {
    	int hashCode = getHash(key);
    	if (chains[hashCode] == null || !chains[hashCode].containsKey(key)) {
    		throw new NoSuchKeyException();
    	} 
    	this.keySize--;
    	return chains[hashCode].remove(key);
    }

    @Override
    public boolean containsKey(K key) {
    	int hashCode = getHash(key);
    	if (chains[hashCode] != null) {
    		return chains[hashCode].containsKey(key);
    	}
        return false;
    }

    @Override
    public int size() {
        return this.keySize;
    }

    @Override
    public Iterator<KVPair<K, V>> iterator() {
        // Note: you do not need to change this method
        return new ChainedIterator<>(this.chains);
    }
    
    /**
     * Hints:
     *
     * 1. You should add extra fields to keep track of your iteration
     *    state. You can add as many fields as you want. If it helps,
     *    our reference implementation uses three (including the one we
     *    gave you).
     *
     * 2. Before you try and write code, try designing an algorithm
     *    using pencil and paper and run through a few examples by hand.
     *
     *    We STRONGLY recommend you spend some time doing this before
     *    coding. Getting the invariants correct can be tricky, and
     *    running through your proposed algorithm using pencil and
     *    paper is a good way of helping you iron them out.
     *
     * 3. Think about what exactly your *invariants* are. As a
     *    reminder, an *invariant* is something that must *always* be 
     *    true once the constructor is done setting up the class AND 
     *    must *always* be true both before and after you call any 
     *    method in your class.
     *
     *    Once you've decided, write them down in a comment somewhere to
     *    help you remember.
     *
     *    You may also find it useful to write a helper method that checks
     *    your invariants and throws an exception if they're violated.
     *    You can then call this helper method at the start and end of each
     *    method if you're running into issues while debugging.
     *
     *    (Be sure to delete this method once your iterator is fully working.)
     *
     * Implementation restrictions:
     *
     * 1. You **MAY NOT** create any new data structures. Iterators
     *    are meant to be lightweight and so should not be copying
     *    the data contained in your dictionary to some other data
     *    structure.
     *
     * 2. You **MAY** call the `.iterator()` method on each IDictionary
     *    instance inside your 'chains' array, however.
     */
    private static class ChainedIterator<K, V> implements Iterator<KVPair<K, V>> {
        private IDictionary<K, V>[] chains;
        private int arrayIndex;
        private Iterator<KVPair<K, V>> iterator;

        public ChainedIterator(IDictionary<K, V>[] chains) {
            this.chains = chains;
            this.arrayIndex = 0;
            this.iterator = null;
        }

        @Override
        public boolean hasNext() {
            boolean result = false;
            while (arrayIndex < chains.length && !result) {
                if (chains[arrayIndex] == null) {
                    iterator = null;
                    arrayIndex++;
                } else {
                    if (iterator == null) {
                        iterator = this.chains[arrayIndex].iterator(); // set iterator
                    } 
                    result = iterator.hasNext();
                    if (!result) {
                    	iterator = null;
                        arrayIndex++;
                    }
                }
            }
            return result;
            
        }

        @Override
        public KVPair<K, V> next() {
            if (!hasNext()) {
            	throw new NoSuchElementException();
            } 
            return iterator.next();
        }
    }
}
