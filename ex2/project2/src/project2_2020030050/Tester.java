package project2_2020030050;

import java.io.IOException;

public class Tester {

	// for BST structures testing
	private BST tree;
	private BSTarray array_tree;

	// for heap structures testing..
	private ArrayHeap heap;
	private DynamicHeap dynamic_heap;

	// null constructor
	public Tester() {
	}

	/*
	 * B I N A R Y S E A R C H T R E E
	 */

	/**
	 * @throws IOException
	 */
	public void test_dynamic_bst() throws IOException {

		MultiCounter.resetCounter(1);
		MultiCounter.resetCounter(3);

		this.tree = new BST();

		long insertion_time1 = 0;
		long insertion_comp1 = 0;

		long deletion_time1 = 0;
		long deletion_comp1 = 0;

		/*
		 * reading elements from files
		 */
		FileHandler handler1 = new FileHandler("keys_1000000_BE.bin");
		int[] integers_to_add = handler1.get_integers();

		FileHandler handler2 = new FileHandler("keys_del_100_BE.bin");
		int[] integers_to_delete = handler2.get_integers();

		/****** INSERTION TESTING (BST DYNAMIC) ******/
		long t1 = System.nanoTime();
		for (int i = 0; i < integers_to_add.length; i++) {
			tree.insert(integers_to_add[i]);
		}
		long t2 = System.nanoTime();

		insertion_time1 = t2 - t1;

		insertion_comp1 = MultiCounter.getCount(1);

		MultiCounter.resetCounter(1);
		MultiCounter.resetCounter(3);

		/****** DELETION TESTING FOR BST (DYNAMIC) ******/
		long t3 = System.nanoTime();
		for (int i = 0; i < integers_to_delete.length; i++) {
			tree.delete(integers_to_delete[i]);
		}
		long t4 = System.nanoTime();

		deletion_time1 = t4 - t3;

 		deletion_comp1 = MultiCounter.getCount(3);

		/****** PRINTING THE RESULTS ******/
		System.out.println("Dynamic BST testing:");
		System.out.println("1.Insertion of 1.000.000 elements");
		System.out.println("Time= " + (double) insertion_time1 / 1_000_000 + " milli-sec");
		System.out.println("Mean comparisons=" + (double) insertion_comp1 / 1_000_000);
		System.out.println("2.Deletion of 100 elements");
		System.out.println("Time= " + (double) deletion_time1 / 1_000_000 + " milli-sec");
		System.out.println("Mean comparisons=" + (double) deletion_comp1 / 100);

	}

	public void test_array_bst() throws IOException {

		MultiCounter.resetCounter(4);
		MultiCounter.resetCounter(6);

		long insertion_time = 0;
		long insertion_comp = 0;

		long deletion_time = 0;
		long deletion_comp = 0;

		/*
		 * reading elements from files
		 */
		FileHandler handler1 = new FileHandler("keys_1000000_BE.bin");
		int[] integers_to_add = handler1.get_integers(); // integers for insertion

		FileHandler handler2 = new FileHandler("keys_del_100_BE.bin");
		int[] integers_to_delete = handler2.get_integers();

		/*
		 * initialization of data structure...
		 */
		this.array_tree = new BSTarray(integers_to_add.length);

		/****** INSERTION TESTING (BST ARRAY) ******/
		long t1 = System.nanoTime();
		for (int i = 0; i < integers_to_add.length; i++) {
			array_tree.insert(integers_to_add[i]);
		}
		long t2 = System.nanoTime();

		insertion_time = t2 - t1;

		insertion_comp = MultiCounter.getCount(4);

		MultiCounter.resetCounter(4);
		MultiCounter.resetCounter(6);

		/****** DELETION TESTING FOR BST (DYNAMIC) ******/
		long t3 = System.nanoTime();
		for (int i = 0; i < integers_to_delete.length; i++) {
			array_tree.delete(integers_to_delete[i]);
		}
		long t4 = System.nanoTime();

		deletion_time = t4 - t3;

		deletion_comp = MultiCounter.getCount(6);

		/****** PRINTING THE RESULTS ******/
		System.out.println("Array BST testing:");
		System.out.println("1.Insertion of 1.000.000 elements");
		System.out.println("Time=" + (double) insertion_time / 1_000_000 + " milli-sec");
		System.out.println("Mean comparisons=" + (double) insertion_comp / 1_000_000);
		System.out.println("2.Deletion of file elements");
		System.out.println("Time=" + (double) deletion_time / 1_000_000 + " milli-sec");
		System.out.println("Mean comparisons=" + (double) deletion_comp / 100);

	}

	
	
	
	
	/*
	 * P R I O R I T Y Q U E U E
	 */
	/**
	 * @throws Exception
	 */
	public void test_array_heap() throws Exception {

		MultiCounter.resetCounter(7);
		MultiCounter.resetCounter(9);

		long insertion_time1 = 0;
		long insertion_comp1 = 0;

		long insertion_time2 = 0;
		long insertion_comp2 = 0;

		long deletion_time = 0;
		long deletion_comp = 0;

		FileHandler handler1 = new FileHandler("keys_1000000_BE.bin");
		int[] integers_to_add = handler1.get_integers();

		FileHandler handler2 = new FileHandler("keys_del_100_BE.bin");
		int[] integers_to_delete = handler2.get_integers();

		/****** 1 BY 1 INSERTION TESTING (ARRAY HEAP) ******/
		this.heap = new ArrayHeap(0, integers_to_add.length);
		long t1 = System.nanoTime();
		for (int i = 0; i < integers_to_add.length; i++) {
			heap.insert(integers_to_add[i]);
		}
		long t2 = System.nanoTime();

		insertion_time1 = t2 - t1;

		insertion_comp1 = MultiCounter.getCount(7);

		MultiCounter.resetCounter(7);
		MultiCounter.resetCounter(9);

		/****** INSERTION TESTING (ARRAY HEAP) ******/
		long t3 = System.nanoTime();
		ArrayHeap heap2 = new ArrayHeap(integers_to_add, integers_to_add.length, integers_to_add.length);
		long t4 = System.nanoTime();

		insertion_time2 = t4 - t3;

		insertion_comp2 = MultiCounter.getCount(9);

		MultiCounter.resetCounter(7);
		MultiCounter.resetCounter(9);

		/****** DELETION TESTING ******/
		long t5 = System.nanoTime();
		for (int i = 0; i < integers_to_delete.length; i++) {
			heap.remove_max();
		}
		long t6 = System.nanoTime();

		deletion_time = t6 - t5;

		deletion_comp = MultiCounter.getCount(9);

		/****** PRINTING THE RESULTS ******/
		System.out.println("ArrayHeap testing:");
		System.out.println("1.Insertion of 1.000.000 elements 1-by-1");
		System.out.println("Time=" + (double) insertion_time1 / 1_000_000 + " milli-sec");
		System.out.println("Comparisons=" + (double) insertion_comp1 / 1_000_000);
		System.out.println("2.Insertion of 1.000.000 elements by \"heapifying\" the input array:");
		System.out.println("Time=" + (double) insertion_time2 / 1_000_000 + " milli-sec");
		System.out.println("Comparisons=" + (double) insertion_comp2 / 1_000_000);
		System.out.println("3.Deletion of 100 (max...) elements");
		System.out.println("Time=" + (double) deletion_time / 1_000_000 + " milli-sec");
		System.out.println("Comparisons=" + (double) deletion_comp / 100);
	}

	public void test_dynamic_heap() throws IOException {

		MultiCounter.resetCounter(10);
		MultiCounter.resetCounter(11);

		long insertion_time1 = 0;
		long insertion_comp1 = 0;

		long insertion_time2 = 0;
		long insertion_comp2 = 0;

		long deletion_time = 0;
		long deletion_comp = 0;

		FileHandler handler1 = new FileHandler("keys_1000000_BE.bin");
		int[] integers_to_add = handler1.get_integers();

		FileHandler handler2 = new FileHandler("keys_del_100_BE.bin");
		int[] integers_to_delete = handler2.get_integers();

		this.dynamic_heap = new DynamicHeap();

		/****** 1 BY 1 INSERTION TESTING (DYNAMIC HEAP) ******/
		long t1 = System.nanoTime();
		for (int i = 0; i < integers_to_add.length; i++) {
			dynamic_heap.insert(integers_to_add[i]);
		}
		long t2 = System.nanoTime();

		insertion_time1 = t2 - t1;

		insertion_comp1 = MultiCounter.getCount(10);

		MultiCounter.resetCounter(10);
		MultiCounter.resetCounter(11);

		/****** INSERTION TESTING (DYNAMIC HEAP) ******/
		long t3 = System.nanoTime();
		DynamicHeap heap2 = new DynamicHeap(integers_to_add);
		long t4 = System.nanoTime();

		insertion_time2 = t4 - t3;

		insertion_comp2 = MultiCounter.getCount(10) + MultiCounter.getCount(11);

		MultiCounter.resetCounter(10);
		MultiCounter.resetCounter(11);

		/****** DELETION TESTING (ARRAY HEAP) ******/
		long t5 = System.nanoTime();
		for (int i = 0; i < integers_to_delete.length; i++) {
			dynamic_heap.remove_max();
		}
		long t6 = System.nanoTime();

		deletion_time = t6 - t5;

		deletion_comp = MultiCounter.getCount(11);

		/****** PRINTING THE RESULTS ******/
		System.out.println("DynamicHeap testing:");
		System.out.println("1.Insertion of 1.000.000 elements 1-by-1");
		System.out.println("Time=" + (double) insertion_time1 / 1_000_000 + " milli-sec");
		System.out.println("Comparisons=" + (double) insertion_comp1 / 1_000_000);
		System.out.println("2.Insertion of 1.000.000 elements by \"heapifying\" the input array:");
		System.out.println("Time=" + (double) insertion_time2 / 1_000_000 + " milli-sec");
		System.out.println("Comparisons=" + (double) insertion_comp2 / 1_000_000);
		System.out.println("3.Deletion of 100 (max...) elements");
		System.out.println("Time=" + (double) deletion_time / 1_000_000 + " milli-sec");
		System.out.println("Comparisons=" + (double) deletion_comp / 100);

	}

	/**
	 * some simple tests to see if the functionalities of the structures work fine:
	 */

	public void simple_test_bst() {

		System.out.println("Testing BST dynamic");

		int[] input = { 8, 3, 10, 1, 6, 4, 11 };

		BST tree = new BST();

		for (int i = 0; i < input.length; i++)
			tree.insert(input[i]);

		tree.print2D();

		System.out.println("---------------------------------");

		// delete leaf 4
		tree.delete(4);

		tree.print2D();

		// delete node 10 with 1 kids
		tree.delete(10);

		System.out.println("---------------------------------");

		tree.print2D();

		// delete node 3 with 2 kids
		tree.delete(3);

		System.out.println("---------------------------------");

		tree.print2D();

		// delete root
		tree.delete(8);

		System.out.println("---------------------------------");

		tree.print2D();

	}

	public void simple_test_bst_array() {

		System.out.println("\nTesting BST array");

		int[] input = { 8, 3, 10, 1, 6, 4, 11 };

		BSTarray tree = new BSTarray(input.length);

		for (int i = 0; i < input.length; i++)
			tree.insert(input[i]);

		tree.preorder();

		// delete leaf 4
		tree.delete(4);

		System.out.println("");

		tree.preorder();

		// delete node 10 with 1 kids
		tree.delete(10);

		tree.preorder();

		System.out.println("");

		// delete node 3 with 2 kids
		tree.delete(3);

		tree.preorder();

		System.out.println("");

		// delete root
		tree.delete(8);

		tree.preorder();

	}

	public void simple_test_heap_array() {

		System.out.println("\n\nTesting Heap array");

		int[] input = { 4, 2, 6, 5, 8, 3, 7 };

		ArrayHeap heap = new ArrayHeap(0, input.length);

		for (int i = 0; i < input.length; i++)
			heap.insert(input[i]);

		heap.display();

		// delete max
		for (int i = 0; i < input.length; i++) {
			heap.remove_max();

			heap.display();

			System.out.println("");
		}
	}
	
	public void simple_test_heap_dynamic() {
		
		System.out.println("\n\nTesting Heap dynamic");
		
		int[] input = { 4, 2, 6, 5, 8, 3, 7 };

		DynamicHeap heap = new DynamicHeap(input); 
		
		heap.print2D();
		
	}
}
