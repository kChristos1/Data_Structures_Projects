package project2_2020030050;

public class Node implements BinNode {

	// class members
	protected int key;
	protected Node left, right;

	// constructor
	public Node(int key, Node left, Node right) {
		this.key = key;
		this.left = left;
		this.right = right;
	}

	public Node() {
		this.key = 0;
		this.left = null;
		this.right = null;
	}

	public Node(int key) {
		this.key = key;
	}

	// class methods
	@Override
	public Node getLeft() {
		return this.left;
	}

	@Override
	public Node getRight() {
		return this.right;
	}

	@Override
	public void setLeft(Node node) {
		this.left = node;
	}

	@Override
	public void setRight(Node node) {
		this.right = node;
	}

	@Override
	public boolean isLeaf() {
		if (left == null && right == null) {
			return true; // current node has no descendant
		}
		return false;
	}

	@Override
	public int getKey() {
		return this.key;
	}

	@Override
	public void setKey(int key) {
		this.key = key;
 	}

}
