package project2_2020030050;

// source code inspired from corresponding tutorial given in class...
// https://www.geeksforgeeks.org/insertion-and-deletion-in-heaps/ 

/**
 * 
 * @author Christos Kostadimas
 * 
 *         MultiCounter.getCount(7); => 1-1 insertion counter
 *         MultiCounter.getCount(9); => all at once insertion AND deletions
 *         counter
 *
 */

public class ArrayHeap {
	private static final int null_value = Integer.MIN_VALUE;
	private int[] Heap; // Pointer to the heap array
	private int max_size; // Maximum size of the heap
	private int n; // Number of elements now in the heap

	public ArrayHeap(int num_of_elements, int max_size) {
		this.Heap = new int[max_size];
		for (int i = 0; i < max_size; i++) {
			Heap[i] = null_value;
		}
		this.max_size = max_size;
		this.n = num_of_elements;
	}

	public ArrayHeap(int[] h, int num, int max_size) // Constructor
	{
		Heap = h;
		n = num;
		this.max_size = max_size;
		build_heap();
	}

	public int heapsize() {
		return max_size;
	}

	public boolean isLeaf(int pos) { // TRUE if pos is a leaf position
		return (pos >= n / 2) && (pos < n);
	}

	// methods to get children of node
	// Return position for left child of pos < 2r+1<n
	public int leftchild(int pos) {
		return 2 * pos + 1;
	}

	// Return position for right child of pos < 2r+2<n
	public int rightchild(int pos) {
		return 2 * pos + 2;
	}

	public int parent(int pos) { // Return position for parent
		if (pos > 0 && pos < n) {
			return (pos - 1) / 2;
		} else {
			return -1;
		}
	}

	public void build_heap() // Heapify contents
	{
		for (int i = n / 2 - 1; i >= 0; i--)
			siftdown(i);
	}

	private void siftdown(int pos) { // Put element in correct place
		while (!isLeaf(pos)) { // Stop if pos is a leaf
			MultiCounter.increaseCounter(9);
			MultiCounter.increaseCounter(9);
			int j = leftchild(pos);
			MultiCounter.increaseCounter(9);
			MultiCounter.increaseCounter(9);
			if ((j < (n - 1)) && (Heap[j] < Heap[j + 1])) {
				j++; // Set j to greater child's value
			}
			MultiCounter.increaseCounter(9);
			if (Heap[pos] >= Heap[j]) {
				return; // Done
			}
			swap(pos, j);
			pos = j; // Move down
		}
	}

	public void insert(int val) { // Insert value into heap
		int curr = n++;
		Heap[curr] = val; // Start at end of heap
		// Now sift up until curr's parent's key > curr's key
		while ((curr != 0) && (Heap[curr] > Heap[parent(curr)])) {
			MultiCounter.increaseCounter(7);
			MultiCounter.increaseCounter(7);
			swap(curr, parent(curr));
			curr = parent(curr);
		}

	}

	// swap objects of positions i,j
	private void swap(int i, int j) {
		int temp = Heap[i];
		Heap[i] = Heap[j];
		Heap[j] = temp;
	}

	public int remove_max() { // Remove minimum value
		swap(0, --n); // Swap minimum with last value
		MultiCounter.increaseCounter(9);
		if (n != 0) { // Not on last element
			siftdown(0); // Put new heap root val in correct place
		}
 		Heap[n] = null_value;
		return Heap[n];
	}

	/*(not counting comparisons , bc for testing we only delete the max value...)*/
	public int remove(int pos) {// Remove value at specified position
		swap(pos, --n); // Swap with last value
		while ((pos != 0) && (Heap[pos] > Heap[parent(pos)])) {
			swap(pos, parent(pos)); // Push up if small key
			pos = parent(pos);
		}
		if (n != 0)
			siftdown(pos); // Push down if large key
		return Heap[n]; // Return deleted value
	}

	// prints array element-by-element...
	public void display() {
		for (int i = 0; i < Heap.length; i++) {
			System.out.print(" " + Heap[i]);
		}
		System.out.println("");
	}

} // Heap class
