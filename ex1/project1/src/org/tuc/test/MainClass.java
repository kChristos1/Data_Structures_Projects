package org.tuc.test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;
import java.util.Random;

import org.tuc.Node;
import org.tuc.hash.Hash;
import org.tuc.list.*;
import org.tuc.test.*;

/**
 * @author Christos Kostadimas
 *
 */
public class MainClass {
	// some constants used in the test runs
	
	public static final int N = 262144;

	public static int MAX_INT_NUMBER = 262144 - 1; // 0<=x<=(N-1) and 0<=y<=N-1 where N=2^18
	
	public static int MIN_INT_NUMBER = 0;

	public static int NUM_OF_SEARCHES = 100;

	public static int M = 10;
	
	public static final int DATA_PAGE_SIZE = 256;

	public static int NUMBER_OF_NODES_TO_ADD[] = { 1000, 9000, 20000, 20000, 20000, 30000 };

	static int NUMBER_OF_NODES_PER_TEST[] = { 1000, 10000, 30000, 50000, 70000, 100000};

	public static void main(String[] args) {
	
		
		Tester tester = new Tester(NUMBER_OF_NODES_TO_ADD, NUMBER_OF_NODES_PER_TEST, MIN_INT_NUMBER, MAX_INT_NUMBER, NUM_OF_SEARCHES);
	   

		System.err.println("Starting test...");
		
		
		try {
			
    	    tester.doTest();
			System.err.println("\n-------------------------------R E S U L T S--------------------------------------");
			System.err.println("\n-----------------------------MEMORY LINKED LIST-----------------------------------\n");
			System.err.println("->for Successful searches:");
			tester.meanComparisonsA1();
	
			System.err.println("\n->for Unuccessful searches:");
			tester.failComparisonsA1();
			
			System.err.println("\n-----------------------------MEMORY HASH TABLE-----------------------------------\n");
			System.err.println("->for Successful searches:");
			tester.meanComparisonsA2();
			
			System.err.println("\n->for Unsuccessful searches:");
			tester.failComparisonsA2();

			System.err.println("\n---------------------------------DISK LIST---------------------------------------\n");
			System.err.println("->for Successful searches:");
			tester.printMeanAccessesB1();
			
			System.err.println("\n->for Unsuccessful searches:");
			tester.printFailAccessesB1();
			
			System.err.println("\n---------------------------------DISK HASH TABLE---------------------------------------\n");

			System.err.println("->for Successful searches:");
			tester.printMeanAccessesB2();
			
			System.err.println("\n->for Unsuccessful searches:");
			tester.printFailAccessesB2();	
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
     	System.err.println("Test done!");	
		
	}
		
}
