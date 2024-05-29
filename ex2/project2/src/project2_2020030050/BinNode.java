package project2_2020030050;

/**
 * @author Christos Kostadimas
 *
 */
public interface BinNode {

	public BinNode getLeft();

	public BinNode getRight();

	public void setLeft(Node node);

	public void setRight(Node node);

	public boolean isLeaf();

	public int getKey();

	public void setKey(int key);

}
