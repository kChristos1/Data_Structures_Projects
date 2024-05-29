package org.tuc;

/**
 * @author Christos Kostadimas
 *
 */
public interface Search {
	
	/**
	 * @param x
	 * @param y
	 */
	public void add(int x, int y);

	/**
	 * @param point
	 */
	public void add(Node point);

	/**
	 * @param x
	 * @param y
	 * @return
	 */
	public long search(int x, int y);

	/**
	 * @param node
	 * @return
	 */
	public long search(Node node);
}
