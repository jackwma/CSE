package datastructures.concrete;


import datastructures.interfaces.IPriorityQueue;
import misc.exceptions.EmptyContainerException;

/**
 * See IPriorityQueue for details on what each method must do.
 */
public class ArrayHeap<T extends Comparable<T>> implements IPriorityQueue<T> {
    // See spec: you must implement a implement a 4-heap.
    private static final int NUM_CHILDREN = 4;

    // You MUST use this field to store the contents of your heap.
    // You may NOT rename this field: we will be inspecting it within
    // our private tests.
    private T[] heap;
    private int currSize; // the current size of the array

    // Feel free to add more fields and constants.

    public ArrayHeap() {
    	this.heap = makeArrayOfT(5);
        this.currSize = 0;    
    }
    
    public ArrayHeap(int k) {
    	heap = makeArrayOfT(k);
    	currSize = 0;
    }

    /**
     * This method will return a new, empty array of the given size
     * that can contain elements of type T.
     *
     * Note that each element in the array will initially be null.
     */
    @SuppressWarnings("unchecked")
    private T[] makeArrayOfT(int size) {
        // This helper method is basically the same one we gave you
        // in ArrayDictionary and ChainedHashDictionary.
        //
        // As before, you do not need to understand how this method
        // works, and should not modify it in any way.
        return (T[]) (new Comparable[size]);
    }

    @Override
    public T removeMin() {
    	if (heap[0] == null) {
    		throw new EmptyContainerException();
    	}
    	T result = heap[0];
    	heap[0] = heap[currSize - 1];
    	heap[currSize - 1] = null;
    	if (currSize > 0) {
    		percolateDown(0);
    	}
    	currSize--;
        return result;
    }
   
    public void percolateDown(int index) {
    	if (currSize > (4 * index)) {
			if (heap[(4* index)] != null && compareChildren(index) != -1) {
				int minIndex = compareChildren(index);
				if (heap[index].compareTo(heap[minIndex]) > 0) {
					T temp = heap[index];
					heap[index] = heap[minIndex];
					heap[minIndex] = temp;
					percolateDown(minIndex);
				} 
			}
    	}
    }
	
    private int compareChildren(int index) {
    	int indexCount = 0;
    	int minIndex = index;
    	while (heap[(4*index) + indexCount] != null && indexCount < 4) {
    		if (heap[minIndex].compareTo(heap[(4 * index) + indexCount]) > 0) {
    			minIndex = (4 * index) + indexCount;
    		}
    		indexCount++;
    	}
    	return minIndex;
    }
    
    @Override
    public T peekMin() {
    	if (currSize == 0) {
    		throw new EmptyContainerException();
    	}
    	return heap[0];
    }

    @Override
    public void insert(T item) {
    	if (item == null) {
    		throw new IllegalArgumentException();
    	}
    	if (currSize == heap.length) { 		//if the array is full
        	this.reSize();
        }
    	heap[currSize] = item;		/**currSize not updated yet**/
    	if (currSize != 0) {
    		for (int i = currSize; i > 0; i = (i-1)/4) {
    			if (heap[i].compareTo(heap[(i-1)/4]) < 0) {
    				heap[i] = heap[(i-1)/4];
    				heap[(i-1)/4] = item;
    			} else {
    				break;
    			}
    		}
    	}
    	currSize++;
    }
    
    // resizing method for heap
    private void reSize() {
    	T[] newHeap = makeArrayOfT(heap.length*2); 
    	for (int i = 0; i < currSize; i++) {
    		newHeap[i] = heap[i];
    	}
    	heap = newHeap;
    }  
    
    @Override
    public int size() {
        return this.currSize;
    }
    
    // replacing a specific element then percolate down
    public void replace(int k, T item) {
        heap[k] = item;
        
    }
    
    public int findIndexOf(T item) {
        for (int i = 0; i < currSize; i++){
            if (heap[i].equals(item)) {
                return i;
            }
        }
        System.out.println("no equal");
        return -1;
    }
    
    
    
    public void percolateUp(int k) {
        if (currSize != 0 && k < currSize) {
            T item = heap[k];
            for (int i = k; i > 0; i = (i-1)/4) {
                if (heap[i].compareTo(heap[(i-1)/4]) < 0) {
                    heap[i] = heap[(i-1)/4];
                    heap[(i-1)/4] = item;
                } else {
                    break;
                }
            }
        }
    }
    
    public void decreasePriority(T prev, T item) {
        int index = this.findIndexOf(prev);
        if (index != -1) {
            System.out.println("update priority");
            this.replace(index, item);
            this.percolateUp(index);
        }
    }
    
    private int findSmallChild(int start, int end) {
        if (start >= this.currSize) {
            return -1;
        } else if (end >= this.currSize) {
            end = this.currSize - 1;
        }
        int min = start;
        for (int i = start; i <= end; i++) {
            if (heap[min].compareTo(heap[i]) > 0) {
                min = i;
            }
        }
        return min;
    }
    
    // peroclate down only when there is a smaller child
    public void percolate(int k) {
        int min = findSmallChild(4 * k + 1, 4 * k + 4);
        if (min != -1 && heap[k].compareTo(heap[min]) > 0) {
            T temp = heap[k];
            heap[k] = heap[min];
            heap[min] = temp;
            percolate(min);
        }
    }
}

