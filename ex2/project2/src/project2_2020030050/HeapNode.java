package project2_2020030050;

public class HeapNode {
	private HeapNode parent, left, right;
	private HeapNode next;
	private HeapNode prev;
	private int key;

	public HeapNode( HeapNode prev, HeapNode parent,int key) {
		this.parent = parent;
	    this.prev = prev;
		this.key = key;
		this.next = null;
		this.right = null;
		this.left = null;
	}
	// setters and getters

	public HeapNode getParent() {
		return parent;
	}

	public HeapNode getPrev() {
		return prev;
	}

	public void setPrev(HeapNode prev) {
		this.prev = prev;
	}

	public HeapNode getNext() {
		return next;
	}

	public void setNext(HeapNode next) {
		this.next = next;
	}

	public void setParent(HeapNode parent) {
		this.parent = parent;
	}

	public HeapNode getLeft() {
		return left;
	}

	public void setLeft(HeapNode left) {
		this.left = left;
	}

	public HeapNode getRight() {
		return right;
	}

	public void setRight(HeapNode right) {
		this.right = right;
	}

	public int getKey() {
		return key;
	}

	public void setKey(int key) {
		this.key = key;
	}

}
