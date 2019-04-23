package datastructures.concrete;

import datastructures.concrete.dictionaries.ChainedHashDictionary;
import datastructures.interfaces.IDictionary;
import datastructures.interfaces.IDisjointSet;

/**
 * See IDisjointSet for more details.
 */
public class ArrayDisjointSet<T> implements IDisjointSet<T> {
    // Note: do NOT rename or delete this field. We will be inspecting it
    // directly within our private tests.
    private int[] pointers;
    private IDictionary<T, Integer> nodeMap;
    private int nodeSize;
    // However, feel free to add more methods and private helper methods.
    // You will probably need to add one or two more fields in order to
    // successfully implement this class.
    
    //constructor with default size of 20
    public ArrayDisjointSet() {
    	this.pointers = new int[20];
        this.nodeMap = new ChainedHashDictionary<>();
        this.nodeSize = 0;
    }
    
    //custom constructor for specific starting capacity 
    public ArrayDisjointSet(int size) {
        this.nodeMap = new ChainedHashDictionary<>();
    	this.pointers = new int[size];
    	this.nodeSize = 0;
    }

    @Override
    public void makeSet(T item) {
    	if (nodeMap.containsKey(item)) {
    		throw new IllegalArgumentException("the item exist in the disjoint set.");
    	} else {
    		int nodeRank = 0;
    		int nodeIndex = nodeSize;
    		nodeSize++;
    		//testLargeForest case here 
    		if (nodeIndex == pointers.length) {
    			resizeSet();
    		}
    		nodeMap.put(item, nodeIndex);
    		pointers[nodeIndex] = -1 * nodeRank - 1;
    	}
    }
    
    //resize the pointers array here 
    private void resizeSet() {
    	int newLength = pointers.length * 2;
    	int[] newPointers = new int[newLength];
    	for (KVPair<T, Integer> each: nodeMap) {
    		newPointers[each.getValue()] = pointers[each.getValue()];
    	}
    	this.pointers = newPointers;
    }
    
    @Override
    public int findSet(T item) {
        if (!nodeMap.containsKey(item)) {
        	throw new IllegalArgumentException("unable to find the item in the disjoint set.");
        } else {
        	int nodeIndex = nodeMap.get(item);
        	return findSet(nodeIndex);
        }
    }
    
    //private helper to findSet using path compression.
    private int findSet(int index) {
    	int nodeRep = pointers[index];
    	if (nodeRep < 0) {
    		//root node case
    		return index;
    	} else {
    		return findSet(nodeRep);
    	}
    }
    
    @Override
    public void union(T item1, T item2) {
        if (!nodeMap.containsKey(item1) || !nodeMap.containsKey(item2)) {
        	throw new IllegalArgumentException();
        }
        int firstRank = findSet(item1);
        int secondRank = findSet(item2);
        if (firstRank == secondRank) { 
        	//already in the same set
        	throw new IllegalArgumentException();
        } else {
        	int firstIndex = -1 * pointers[firstRank] - 1;
        	int secondIndex = -1 * pointers[secondRank] - 1;
        	if (firstIndex > secondIndex) {
        		pointers[secondRank] = firstRank;
        	} else if (firstIndex < secondIndex) {
        		pointers[firstRank] = secondRank;
        	} else {
        		//random case for if they are equal, order doesn't matter
        		pointers[firstRank] = secondRank;
        		int newRank = secondRank + 1;
        		pointers[secondRank] = -1 * newRank - 1;
        	}
        }
        
    }
}
