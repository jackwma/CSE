package datastructures.concrete;

// import datastructures.concrete.DoubleLinkedList.Node;
import datastructures.interfaces.IList;
import misc.exceptions.EmptyContainerException;
// import misc.exceptions.NotYetImplementedException;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
// import java.util.*;

/**
 * Note: For more info on the expected behavior of your methods, see
 * the source code for IList.
 */
public class DoubleLinkedList<T> implements IList<T> {
    // You may not rename these fields or change their types.
    // We will be inspecting these in our private tests.
    // You also may not add any additional fields.
    private Node<T> front;
    private Node<T> back;
    private int size;

    public DoubleLinkedList() {
        this.front = null;
        this.back = null;
        this.size = 0;
    }
    
    private static class Node<E> {
        // You may not change the fields in this node or add any new fields.
        public final E data;
        public Node<E> prev;
        public Node<E> next;

        public Node(Node<E> prev, E data, Node<E> next) {
            this.data = data;
            this.prev = prev;
            this.next = next;
        }

        public Node(E data) {
            this(null, data, null);
        }
        
        // the imported node pre cannot be a front node
        public Node(Node<E> pre, E newData){
            this(pre.prev, newData, pre.next);
        }
        
        // Feel free to add additional constructors or methods to this class.
    }
    
    @Override
    public void add(T item) {
        if (this.front != null){
            Node<T> added = new Node<T>(this.back, item, null);
            this.back.next = added;
            this.back = this.back.next;
        } else {
            this.front = new Node<T>(item);
            this.back = this.front;
        }
        size++;
    }

    @Override
    public T remove() {
    	if (back == null) {
    		throw new EmptyContainerException();
    	}
    	T result = this.back.data;
    	if (this.size == 1){
    	    front = null;
    	    back = null;
    	} else {
    	    back = back.prev;
            back.next = null;
    	}
    	this.size--;
    	return result;
    }

    // If the index is not within the size of the DoubleLinkedList
    //  throw an IndexOutOfBoundsException()
    // Returns the item located at the given index.
    @Override
    public T get(int index) {
    	if (index >= size() || index < 0) {
    		throw new IndexOutOfBoundsException();
    	}
    	Node<T> temp;
    	if (index * 2 < size()) {
    		temp = this.front;
        	for (int i=0; i<index; i++){            // iterates index times to get to the node
        	    temp = temp.next;
        	}
    	} else {
    		temp = this.back;
        	for (int i= size() - 1; i>index; i--){           
        	    temp = temp.prev;
        	}
    	}
    	
    	if (temp == null) {
    	    System.out.println("null");
    	}
    	return temp.data;
    }

    @Override
    public void set(int index, T item) {
    	if (index >= size() || index < 0) {
    		throw new IndexOutOfBoundsException();
    	}
    	Node<T> temp = this.front;
        for (int i=0; i<index; i++){            // iterates index times to get to the node
            temp = temp.next;
        }
        temp = new Node<T>(temp, item);         // replacing the node to be overwritten
        if (front != back) {
            if (index == 0) {                    // the case that replaces the front
                front = temp;
                temp = temp.next;
                temp.prev = front;               
            } else if (index == size()-1) {     // the case that replaces the back
                back = temp;
                temp = temp.prev;
                temp.next = back;
            } else {                           // general case
                Node<T> prev = temp.prev;
                prev.next = temp;
                prev = temp.next;
                prev.prev = temp;                      // change the links to the replaced nodes
            }
        } else {
            front = temp;
            back = temp;
        }
    }

    @Override
    public void insert(int index, T item) {
    	if (index >= size() + 1 || index < 0) {
    		throw new IndexOutOfBoundsException();
    	}
    	if (index == this.size()){
    	    this.add(item);
    	} else if (index == 0) {
    	    Node<T> temp = this.front;
    	    this.front = new Node<T>(null, item, temp);
    	    this.front.next.prev = this.front;
    	    size++;
    	} else {
    	    Node<T> previous = findCurr(index);
    	    Node<T> curr = previous.next;          // keeps track of the node at the index
    	    previous.next = new Node<T>(previous, item, curr); // inserts the new node
    	    curr.prev = previous.next;
    	    size++;
    	}	
    }

    /**
     * Deletes the item at the given index. If there are any elements located at a higher
     * index, shift them all down by one.
     * 
     * @throws IndexOutOfBoundsException if the index < 0 or index >= this.size()
     */
    @Override
    public T delete(int index) {
    	if (index >= size() || index < 0) {
    		throw new IndexOutOfBoundsException();
    	}
    	Node<T> temp = front;
    	T result;
    	if (index == 0) {
    		result = front.data;
    		front = temp.next;
    	} else if (index == this.size()-1){
            return this.remove();   
        } else {
            temp = findCurr(index);
            result = temp.next.data;
            temp.next = temp.next.next;       // removing the connection to the node at given index
            temp.next.prev = temp;
        }
    	this.size--;
    	return result;
    }
    
    // pre: import the index of the node to be deleted
    // post: return the node at index-1
    private Node<T> findCurr(int index){
        Node<T> temp;
        if (index>this.size()/2){               // for efficiency, this decides looping from back 
            temp = this.back;              // or front
            for (int i=this.size(); i>index; i--){
                temp = temp.prev;
            }
        } else {
            temp = this.front;
            for (int i=0; i<index-1; i++){          // iterates to the node before the index node
                temp = temp.next;
            }
        }
        return temp;
    }

    @Override
    public int indexOf(T item) {
	    Node<T> temp = front;
	    int index = 0;
	    while (temp != null) {
		    if (Objects.equals(temp.data, item)) {
		    	return index;
		    }
		    temp = temp.next;
		    index++;
	    }
	    return -1;
    }

    @Override
    public int size() {
    	return this.size;
    }

    @Override
    public boolean contains(T other) {
	    Node<T> temp = front;
	    while (temp != null) {
		    if (Objects.equals(temp.data, other)) {
		    	return true;
		    }
		    temp = temp.next;
		    }
	    return false; 
    }

    @Override
    public String toString(){
        String out = "";
        for (T node : this) {
            out = out + node.toString();
        }
        return out;
    }

    @Override
    public Iterator<T> iterator() {
        // Note: we have provided a part of the implementation of
        // an iterator for you. You should complete the methods stubs
        // in the DoubleLinkedListIterator inner class at the bottom
        // of this file. You do not need to change this method.
        return new DoubleLinkedListIterator<>(this.front);
    }

    

    private static class DoubleLinkedListIterator<T> implements Iterator<T> {
        // You should not need to change this field, or add any new fields.
        private Node<T> current;

        public DoubleLinkedListIterator(Node<T> current) {
            // You do not need to make any changes to this constructor.
            this.current = current;
        }

        /**
         * Returns 'true' if the iterator still has elements to look at;
         * returns 'false' otherwise.
         */
        public boolean hasNext() {
            if (current!=null){
                return true;
            }
            return false;
        }

        /**
         * Returns the next item in the iteration and internally updates the
         * iterator to advance one element forward.
         *
         * @throws NoSuchElementException if we have reached the end of the iteration and
         *         there are no more elements to look at.
         */
        public T next() {
            if (!this.hasNext()){
                throw new NoSuchElementException();
            }
            T value = current.data;
            current = current.next;
            return value;
        }
    }
}
