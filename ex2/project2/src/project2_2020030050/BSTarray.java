package project2_2020030050;

/**
 * @author Christos Kostadimas
 * 
 *         Binary search tree array implementation. A 3xN integer array has been
 *         used to store the nodes of bst. Each column of the array represents a
 *         node. row #0 stores the info-key of the node, row #1 stores the index
 *         of the left child row #2 stores the index of the right child and is
 *         also used to implement the stack of the available position in the
 *         array where we can add a new node.
 * 
 *         Generally this class's methods are pretty similar to the BST class
 *         methods.
 * 
 *         MultiCounter.getCount(4) => for bst array insertion comparisons
 *         MultiCounter.getCount(5) => for bst array searching comparisons
 *         MultiCounter.getCount(6) => for bst array deletion comparisons
 *
 */
public class BSTarray implements BinSearchTree {

	// class variables
	private int array[][];
	private int N; // size of data structure (maximum value of nodes that this structure can hold)
	private int avail; // #of row of the next available position that can hold a new node
	private int root;
	// private int num_of_nodes;
	// some constants
	public static final int null_value = Integer.MIN_VALUE;
	public static final int info = 0;
	public static final int left = 1;
	public static final int right = 2;

	// class constructor
	public BSTarray(int N) {
		this.root = null_value;
		this.N = N;
		this.array = new int[3][N];
		this.init_array();
		this.avail = 0;
		// this.num_of_nodes = 0;
	}

	// class methods
	/**
	 * first two columns of the array have to be equal to null_value (empty..) The
	 * last column of the array should be the "stack" of the available positions
	 * First available position is 0, second available position is 1 ... and so goes
	 * on..
	 */
	public void init_array() {
		for (int i = 0; i < 2; i++) {
			for (int j = 0; j < this.N; j++) {
				this.array[i][j] = null_value;
			}
		}

		for (int p = 0; p < this.N; p++) {
			this.array[right][p] = p + 1;
		}
		// making the value of last node to be null (end of available spots..)
		this.array[right][this.N - 1] = null_value;
	}

	/**
	 * method to "give us" a column of the array to use for insertion... returns
	 * null_value when theres no more space...
	 * 
	 * @return position (column) of array that will be used for insertion
	 */
	private int get_node() { // Get next available node
		MultiCounter.increaseCounter(4);
		if (avail == null_value ) {
			return null_value;
		} else {
			int pos = avail;
			avail = array[right][avail]; // updating available position avail...
			return pos;
		}
	}

	/**
	 * method to free up a position of the array after deletion of data..
	 * 
	 * @param p
	 */
	private void free_node(int p) { // make node available
		this.array[info][p] = null_value;
		this.array[right][p] = avail; // link "new"-freed up available node with the available nodes that already
										// exist.
										// "push node back on stack"
		avail = p; // update avail
	}

	/**
	 * methods to INSERT a node in the structure
	 */
	@Override
	public void insert(int data) {
		root = help_insert(root, data);
	}

	private int help_insert(int node, int data) {
		// 1st CASE: root is empty, start "building" the binary search tree in the array
		// "empty tree, add only the root"			
		MultiCounter.increaseCounter(4);
		if (node == null_value) {
			int pos = this.get_node();
			MultiCounter.increaseCounter(4);
			if (pos == null_value) {
				return node;
			}
			node = pos;
			array[info][pos] = data;
			array[left][pos] = null_value;
			array[right][pos] = null_value;
		} else {

			/*
			 * 2nd CASE: data value we want to add is less than the value of the root (we
			 * must -recursively- add the data in the left subtree of the root)
			 */
			MultiCounter.increaseCounter(4);
			MultiCounter.increaseCounter(4);
			if (data < array[info][node]) {
				array[left][node] = help_insert(array[left][node], data);
			}

			/*
			 * 3rd CASE data value we want to add is greater than the value of the root (we
			 * must -recursively- add the data in the right subtree of the root)
			 */
			else if (data > array[info][node]) {
				array[right][node] = help_insert(array[right][node], data);
			}
		}

		// in each case we return the node index...
		return node;
	}

	// method to check if the structure is empty
	public boolean isEmpty() {
		return this.root == null_value;
	}

	/**
	 * Methods to delete a key from the structure:
	 */
	@Override
	public void delete(int k) {
		root = help_delete(root, k);
	}

	private int help_delete(int root, int data) {
		MultiCounter.increaseCounter(6);
		if (root == null_value) { // if tree/subtree is empty, return
			return root;
		}

		// traverse the tree
		MultiCounter.increaseCounter(6);
		MultiCounter.increaseCounter(6);
		if (data < array[info][root]) {
			// traversing the left subtree to find node to be deleted
			array[left][root] = help_delete(array[left][root], data);
		} else if (data > array[info][root]) {
			// traversing the right subtree to find the node to be deleted
			array[right][root] = help_delete(array[right][root], data);
		}

		else {
			// Node to be deleted has one child.
			MultiCounter.increaseCounter(6);				
			MultiCounter.increaseCounter(6);
			if (array[left][root] == null_value) {
				return array[right][root];
			} else if (array[right][root] == null_value) {
				return array[left][root];
			}

			// node to be deleted has 2 children
			// get inorder successor
			array[info][root] = this.getmin(array[right][root]);

			// delete inorder successor
			array[right][root] = help_delete(array[right][root], array[info][root]);
		}
		// this.free_node(root);
		return root;
	}

	// returning min value of subtree underneath "node"
	private int getmin(int node) {
		int minv = array[info][node];
		while (array[left][node] != null_value) {
			MultiCounter.increaseCounter(6);// comparison for deletion...
			minv = array[left][node];
			node = array[left][node];

		}
		return minv;
	}

	@Override
	public void search(int val) {
		this.help_search(root, val);
	}

	private int help_search(int node, int val) {
		MultiCounter.increaseCounter(5);
		if ((node == null_value || array[info][node] == val) ) {
			// Base Cases: root is null or key is present at root
			return node;
		}
		MultiCounter.increaseCounter(5);
		if (val < array[info][node]) {
			// go to left subtree
			return help_search(array[left][node], val);
		}

		else {
			// go to right subtree
			return help_search(array[right][node], val);
		}

	}

	public void preorder() {
		help_preorder(root);
	}

	private void help_preorder(int r) {
		if (r != null_value) {
			System.out.print(array[info][r] + " ");
			help_preorder(array[left][r]);
			help_preorder(array[right][r]);
		}
	}

	public void postorder() {
		this.help_postorder(root);
	}

	public void help_postorder(int r) {
		if (r != null_value) {
			help_postorder(array[left][r]);
			help_postorder(array[right][r]);
			System.out.print(array[info][r] + " ");
		}
	}

	public void inorder() {
		this.help_inorder(root);
	}

	public void help_inorder(int r) {
		if (r != null_value) {
			help_inorder(array[left][r]);
			System.out.print(array[info][r] + " ");
			help_inorder(array[right][r]);
		}
	}

	public void print_array() {
		for (int i = 0; i < this.N; i++) {
			System.out.print(array[right][i] + " ");
		}
	}

	public int info(int p) {
		return this.array[info][p];
	}

	public int left(int p) {
		return this.array[left][p];
	}

	public int right(int p) {
		return this.array[right][p];
	}

	// setters and getters

	public int getN() {
		return N;
	}

	public void setN(int n) {
		N = n;
	}

	public int getAvail() {
		return avail;
	}

	public void setAvail(int avail) {
		this.avail = avail;
	}

	public int getRoot() {
		return root;
	}

	public void setRoot(int root) {
		this.root = root;
	}

	public int[][] getArray() {
		return array;
	}

	public void setArray(int[][] array) {
		this.array = array;
	}

}
