
package project2_2020030050;

//implementation from : https://www.softwaretestinghelp.com/binary-search-tree-in-java/ 
//with a few changes... 
//method to print 2d tree from : https://www.geeksforgeeks.org/print-binary-tree-2-dimensions/

/**
 * 
 * @author Christos Kostadimas
 * 
 *         Class to implement dynamic binary search tree. Methods/operations
 *         implemented: 
 *        -Insertion of random key
 *        -Searching random key 
 *        -Deleting random key
 * 
 *         MultiCounter.getCount(1) => for insertion comparisons
 *         MultiCounter.getCount(2) => for search comparisons
 *         MultiCounter.getCount(3) => for deletion comparisons
 * 
 */

public class BST implements BinSearchTree {

	private Node root;

	// constructor
	public BST() {
		root = null;
	}

	// delete a Node from BST
	public void delete(int key) {
		root = delete_Recursive(root, key);
	}

	// recursive delete function
	private Node delete_Recursive(Node root, int key) {
		// tree is empty
		MultiCounter.increaseCounter(3);
		if (root == null) {
			return root;
		}

		// traverse the tree
		MultiCounter.increaseCounter(3);
		MultiCounter.increaseCounter(3);
		if (key < root.key) { // traverse left subtree
			root.left = delete_Recursive(root.left, key);
		} else if (key > root.key) { // traverse right subtree
			root.right = delete_Recursive(root.right, key);
		} else {
			// Node contains only one child
			MultiCounter.increaseCounter(3);
			MultiCounter.increaseCounter(3);
			if (root.left == null) {
				return root.right;
			} else if (root.right == null) {
				return root.left;
			}
			// Node has two children;
			// get inorder successor (min value in the right subtree)
			root.key = minValue(root.right);

			// Delete the inorder successor
			root.right = delete_Recursive(root.right, root.key);
		}
		return root;
	}

	int minValue(Node root) {
		// initially minval = root
		int minval = root.key;
		// find minval
		while (root.left != null) {
			MultiCounter.increaseCounter(3);
			minval = root.left.key;
			root = root.left;
		}
		return minval;
	}

	// insert a Node in BST
	public void insert(int key) {
		root = insert_Recursive(root, key);
	}

	// recursive insert function
	private Node insert_Recursive(Node root, int key) {
		// tree is empty
		MultiCounter.increaseCounter(1);
		if (root == null) {
			root = new Node(key);
			return root;
		}
		// traverse the tree
		MultiCounter.increaseCounter(1);
		MultiCounter.increaseCounter(1);
		if (key < root.key) { // insert in the left subtree
			root.left = insert_Recursive(root.left, key);
		} else if (key > root.key) { // insert in the right subtree
			root.right = insert_Recursive(root.right, key);
		}
		// return pointer
		return root;
	}

    // method for inorder traversal of BST 
	public void inorder() {
		inorder_Recursive(root);
	}

	// recursively traverse the BST
	private void inorder_Recursive(Node root) {
		if (root != null) {
			inorder_Recursive(root.left);
			System.out.print(root.key + " ");
			inorder_Recursive(root.right);
		}
	}
	
	public void preorder() {
		preorder_Recursive(root);
	}

	// recursively traverse the BST
	private void preorder_Recursive(Node root) {
		if (root != null) {
			System.out.print(root.key + " ");
			preorder_Recursive(root.left);
			preorder_Recursive(root.right);
		}
	}

	public void postorder() {
		postorder_Recursive(root);
	}

	// recursively traverse the BST
	private void postorder_Recursive(Node root) {
		if (root != null) {
			System.out.print(root.key + " ");
			postorder_Recursive(root.left);
			postorder_Recursive(root.right);
		}
	}


	public void search(int key) {
		root = search_Recursive(root, key);
	}

	// recursive insert function
	private Node search_Recursive(Node root, int key) {
		// Base Cases: root is null or key is present at root
		MultiCounter.increaseCounter(2);
		if (root == null || root.key == key) {
			return root;
		}
		// val is greater than root's key
		MultiCounter.increaseCounter(2);
		if (root.key > key) {
			return search_Recursive(root.left, key);
		}
		// val is less than root's key
		return search_Recursive(root.right, key);
	}
	

	/*
	 * from : https://www.geeksforgeeks.org/print-binary-tree-2-dimensions
	 */
	// Wrapper over print2DUtil()
	public void print2D() {
		// Pass initial space count as 0
		print2DUtil(root, 0);
	}

	// Function to print binary tree in 2D
	// It does reverse inorder traversal
	private void print2DUtil(Node root, int space) {
		// Base case
		if (root == null)
			return;

		// Increase distance between levels
		space += 10;

		// Process right child first
		print2DUtil(root.right, space);

		// Print current HeapNode after space
		// count
		System.out.print("\n");
		for (int i = 10; i < space; i++)
			System.out.print(" ");
		System.out.print(root.key + "\n");

		// Process left child
		print2DUtil(root.left, space);
	}

	

}// BST class
