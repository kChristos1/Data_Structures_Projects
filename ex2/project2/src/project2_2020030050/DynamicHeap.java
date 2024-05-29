package project2_2020030050;

/**
 * 
 * @author Christos Kostadimas
 * 
 *         Class that implements a dynamic priority queue (max heap).
 *         Priority queue is always a complete binary (not search...) tree 
 *       
 *         MultiCounter.getCount(10) => for 1-1 insertion comparisons
 *         MultiCounter.getCount(11) => for deletion comparisons
 * 
 */

public class DynamicHeap {

	/**
	 * root contains the biggest integer...
	 */
	// class variables
	private HeapNode root;
	private HeapNode last; // last occupied node in structure...
	private HeapNode current;

	// constructors
	public DynamicHeap(int[] input) {
		this.root = null;
		this.last = null;
		for (int i = 0; i < input.length; i++) {
			root = help_insert(root, input[i]);
		}
		heapify(current);
	}

	public DynamicHeap() {
		this.current = null;
		this.root = null;
		this.last = null;
	}

	public void insert(int key) {
		root = help_insert(root, key);
		bubble_up(last);
	}

	// method that builds a binary complete tree (arranged by the order of
	// insertion.. not yet a heap/queue)
	private HeapNode help_insert(HeapNode node, int key) {
		MultiCounter.increaseCounter(10); 
		if (node == null) {
			// if root is null, queue is empty, start building it
			node = new HeapNode(null, null, key);
			last = node; // updating last
			current = node; // current updated..
			return node;
		} else {
			MultiCounter.increaseCounter(10); 
			MultiCounter.increaseCounter(10); 
			if (current.getLeft() != null && current.getRight() != null) {
				current = current.getNext();
			}
			HeapNode new_node = new HeapNode(last, current, key); // prev = last and parent = current (for the new
																	// HeapNode i just added..)
			last.setNext(new_node);
			last = new_node;
			MultiCounter.increaseCounter(10); 
			if (current.getLeft() == null) {
				current.setLeft(last);
			} else {
				current.setRight(last);
			}
		}
		return node;

	}

	private void bubble_up(HeapNode node) {
		while (node != root && node.getKey() > node.getParent().getKey()) {
			MultiCounter.increaseCounter(10); 
			MultiCounter.increaseCounter(10); 
			swap(node.getParent(), node);
			node = node.getParent();
		}
	}

	private void swap(HeapNode node1, HeapNode node2) {
		int temp = node1.getKey();
		node1.setKey(node2.getKey());
		node2.setKey(temp);
	}

	public void remove_max() {
		root = remove_max_help(root);
	}
	
	public void help_heapify(HeapNode node) {
		while (!(node.getLeft() == null && node.getRight() == null)) {
			MultiCounter.increaseCounter(11);
			HeapNode maxChild = node.getLeft();
			MultiCounter.increaseCounter(11);
			MultiCounter.increaseCounter(11);
			if (node.getRight() != null && maxChild.getKey() < node.getRight().getKey()) {
				maxChild = node.getRight();
			}
			MultiCounter.increaseCounter(11);
			if (node.getKey() > maxChild.getKey()) {
				return;
			}
			swap(node, maxChild);
			node = maxChild;
		}

	}

	private HeapNode remove_max_help(HeapNode root) {
		MultiCounter.increaseCounter(11);
		if (root == null) {
			return root; // heap is empty, there is nothing to delete
		} else {
			MultiCounter.increaseCounter(11);
			if (root.getNext() == null) { // delete the root (max) when its the only elemnt in the structure
				root = null;
				last = null;
				return root;
			}
			// if there are more elements
			swap(root, last); // swap root with the last element
			last.setKey(Integer.MIN_VALUE); // "delete" the last element
			heapify(root);
			last = last.getPrev(); // last has been deleted, update it
			last.setNext(null);
			current = last.getParent(); // update current
			return root;
		}
	}

	// Function to print binary tree in 2D
	// It does reverse inorder traversal
	private void print2DUtil(HeapNode root, int space) {
		// Base case
		if (root == null)
			return;
		// Increase distance between levels
		space += 10;
		// Process right child first
		print2DUtil(root.getRight(), space);
		// Print current HeapNode after space
		// count
		System.out.print("\n");
		for (int i = 10; i < space; i++)
			System.out.print(" ");
		System.out.print(root.getKey() + "\n");
		// Process left child
		print2DUtil(root.getLeft(), space);
 	}

	// Wrapper over print2DUtil()
	public void print2D() {
		// Pass initial space count as 0
		print2DUtil(root, 0);
	}

	public void heapify(HeapNode current) {
		while (true) {
			help_heapify(current);
			MultiCounter.increaseCounter(11);
			if (current == root) {
				break;
			}
			current = current.getPrev();
		}
	}


}
