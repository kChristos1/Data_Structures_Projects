package org.tuc;

import java.io.Serializable;

/**
 * @author Christos Kostadimas
 *
 */

public class Node {

	// member variables
	private int x;
	private int y;
	private Node next;

	//class constructor
	public Node(int x, int y) {
		this.x = x;
		this.y = y;
	}

	// setters and getters:
	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public Node getNext() {
		return next;
	}

	public void setNext(Node next) {
		this.next = next;
	}

}
