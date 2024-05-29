package project2_2020030050;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Random;

public class MainClass {

	public static void main(String Args[]) throws Exception {

		Tester testing = new Tester();

		testing.test_dynamic_bst();

		System.out.println("");

		testing.test_array_bst();

		System.out.println("");

		testing.test_array_heap();

		System.out.println("");

		testing.test_dynamic_heap();

		/*
		  testing.simple_test_bst();
		  
		  testing.simple_test_bst_array();
		 
		  testing.simple_test_heap_array();
		  
		  testing.simple_test_heap_dynamic();
		  
		 */
	}

}
