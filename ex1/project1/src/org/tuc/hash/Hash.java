package org.tuc.hash;

import org.tuc.Node;
import org.tuc.Search;
import org.tuc.list.List;
import org.tuc.test.MainClass;

/**
 * 
 * @author Christos Kostadimas
 *
 */

public class Hash implements Search {

	// member variables
	private List[] hashTable;
	private boolean flag; // flag that tells us if searching for a node was successful or not

	// constructor
	public Hash() {
		this.hashTable = new List[MainClass.M];
	}

	// class methods

	/**
	 * Hashing function
	 * 
	 * @param x
	 * @param y
	 * @return position list array
	 */
	public static int hashFunc(int x, int y) {
		return (int) (((long) x * MainClass.N + y) % MainClass.M);
	}

	/**
	 * Method for adding a node in the hashTable (with the node as argument).
	 * Inherited from Search interface (see org.tuc package). This method first
	 * using hashing, finds the pos of the hashTable array to see where the node has
	 * to go and then uses the tail reference of the corresponding list to add the
	 * element without traversing the list, this happens by invoking the "add(Node
	 * node)"method of list class.
	 * 
	 */
	@Override
	public void add(Node newNode) {
		int x = newNode.getX();
		int y = newNode.getY();

		int pos = hashFunc(x, y);

		if (hashTable[pos] == null) {
			hashTable[pos] = new List();
		}
		hashTable[pos].add(newNode);
	}

	/**
	 * Same as add(Node node) but with the nodes coordinates x and y as arguments
	 */
	@Override
	public void add(int x, int y) {
		Node newNode = new Node(x, y);
		int pos = hashFunc(x, y);

		if (hashTable[pos] == null) {
			hashTable[pos] = new List();
		}
		hashTable[pos].add(newNode);
	}

	/**
	 * method for searching a node from the hashTable (with the node as argument),
	 * inherited from Search interface too. Tells us if searching was successful or
	 * not y updating flag variable and returns number of comparisons made while
	 * searching (this happens by invoking "search(Node node)" method from List
	 * class.
	 *
	 */
	@Override
	public long search(Node node) {
		long comparisons = 0;
		int x = node.getX();
		int y = node.getY();
		int pos = hashFunc(x, y);

		// in case pos is empty, there's nothing to search there
		if (this.hashTable[pos] == null) {
			this.flag = false;
			return comparisons;
		}
		comparisons = this.hashTable[pos].search(node);
		this.flag = hashTable[pos].getFlag();
		return comparisons;
	}

	/**
	 * Same as the above but with the nodes coordinates x and y as argument.
	 *
	 */
	@Override
	public long search(int x, int y) {
		long comparisons = 0;

		int pos = hashFunc(x, y);
		// in case pos is empty, there's nothing to search there
		if (this.hashTable[pos] == null) {
			this.flag = false;
			return comparisons;
		}
		comparisons = this.hashTable[pos].search(x, y);
		this.flag = hashTable[pos].getFlag();
		return comparisons;
	}

	// setters and getters

	public List[] getHashTable() {
		return hashTable;
	}

	public void setHashTable(List[] hashTable) {
		this.hashTable = hashTable;
	}

	public boolean getFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}

}
