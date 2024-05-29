package org.tuc.list;

import java.io.IOException;

import org.tuc.Node;
import org.tuc.Search;
import org.tuc.counter.MultiCounter;

/**
 * @author Christos Kostadimas
 *
 */
public class List implements Search {

	// class variables
	private Node head; // pointing in the first element of the list
	private Node tail; // pointing in the last (most recent) element of the list
	private boolean flag; // flag that tells us if searching for a node was successful or not

	// constructor
	public List() {
		this.head = null;
		this.tail = null;
	}

	// class methods
	/**
	 * method to add element to the list (without traversing the list)
	 */
	@Override
	public void add(Node newNode) {
		if (head == null) {
			// list is empty, we add the first element
			head = newNode;
			tail = newNode;
		} else {
			tail.setNext(newNode);
		}
		tail = newNode;

	}

	/**
	 * method to add element to the list (without traversing the list)
	 */
	@Override
	public void add(int x, int y) {
		Node newNode = new Node(x, y);
		System.out.println("here");

		if (this.head == null) {
			// list is empty, we add the first element
			this.head = newNode;
			this.tail = newNode;
		} else {
			tail.setNext(newNode);
		}
		// updating tail
		tail = newNode;

	}
	
	
	/**
	 * returns number of comparisons between (x,y) made to find Node "node" also
	 * updates flag to true or false each time if searching was successful =>
	 * flag=true if searching failed (did not find element) => flag = false
	 */
	@Override
	public long search(Node node) {
		long numOfComparisons = 0;
		int x = node.getX();
		int y = node.getY();
		Node current = this.head;
		// reseting the counter(2)
		MultiCounter.resetCounter(2);
		while (MultiCounter.increaseCounter(2) && !(current.getX() == x && current.getY() == y)) {
			//MultiCounter.increaseCounter(2);
			numOfComparisons = MultiCounter.getCount(2);
			current = current.getNext();
			if (current == null) {
				// reached end of list without finding the wanted element
				this.flag = false;
				return numOfComparisons;
			}
		}
		this.flag = true;
		//MultiCounter.increaseCounter(2);
		numOfComparisons = MultiCounter.getCount(2);
		return numOfComparisons;
	}

	/**
	 * returns number of comparisons between (x,y) made to find node (x,y) also
	 * updates flag to true or false each time if searching was successful =>
	 * flag=true if searching failed (did not find element) => flag = false
	 */
	@Override
	public long search(int x, int y) {
		long numOfComparisons = 1;
		Node current = this.head;
		// reseting the counter(1)
		MultiCounter.resetCounter(1);
		while (MultiCounter.increaseCounter(1) && !(current.getX() == x && current.getY() == y)) {
			numOfComparisons = MultiCounter.getCount(1);
			current = current.getNext();
			if (current == null) {
				// reached end of list without finding the wanted element
				this.flag = false;
				return numOfComparisons;

			}
		}
		this.flag = true;
		numOfComparisons = MultiCounter.getCount(1);
		return numOfComparisons;
	}
	

	// setters and getters
	public Node getHead() {
		return head;
	}

	public void setHead(Node head) {
		this.head = head;
	}

	public Node getTail() {
		return tail;
	}

	public void setTail(Node tail) {
		this.tail = tail;
	}

	public boolean getFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}

}
