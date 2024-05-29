package org.tuc.test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Random;

import org.tuc.Node;
import org.tuc.Search;
import org.tuc.counter.MultiCounter;
import org.tuc.hash.DiskHash;
import org.tuc.hash.Hash;
import org.tuc.list.DiskList;
import org.tuc.list.List;
import java.util.ArrayList;

/**
 * @author Christos Kostadimas, AM:2020030050
 *
 */
public class Tester {

	private int[] nodesPerTest; // array of number of nodes per test
	private int[] nodesToAdd; // array of number of node that have to be added (appended) for each test
	private int minIntNumber; // the lower bound of the generated keys to use
	private int maxIntNumber; // the upper bound of the generated keys to use (N)
	private int numOfSearches; // searches
	private double[] meanComparisonsA1; 
	private double[] meanFailComparisonsA1;
	private double[] meanComparisonsA2;
	private double[] meanFailComparisonsA2;
	private double[] meanAccessesB1;
	private double[] meanFailAccessesB1;
	private double[] meanAccessesB2;
	private double[] meanFailAccessesB2;

	// class constructor
	public Tester(int[] nodesToAdd, int[] nodesPerTest, int minIntNumber, int maxIntNumber, int numOfSearches) {
		this.nodesPerTest = nodesPerTest;
		this.nodesToAdd = nodesToAdd;
		this.minIntNumber = minIntNumber;
		this.maxIntNumber = maxIntNumber;
		this.numOfSearches = numOfSearches;
		this.meanComparisonsA1 = new double[nodesPerTest.length]; 
		this.meanFailComparisonsA1 = new double[nodesPerTest.length];
		this.meanComparisonsA2 = new double[nodesPerTest.length]; 
		this.meanFailComparisonsA2 = new double[nodesPerTest.length]; 
		this.meanAccessesB1 = new double[nodesPerTest.length]; 
		this.meanFailAccessesB1 = new double[nodesPerTest.length];
		this.meanAccessesB2 = new double[nodesPerTest.length];
		this.meanFailAccessesB2 = new double[nodesPerTest.length];
	}

	/**
	 * Method that returns an array of Nodes with length = numberOfNodes containing
	 * random nodes with x and y's between minIntNumber and maxIntNumber values.
	 * 
	 * @param numberOfNodes
	 * @return
	 */
	public Node[] getRandomNodes(int numberOfNodes) {
		Node[] randomPoints = new Node[numberOfNodes];
		Random randomGenerator = new Random();

		int[] randomX = randomGenerator.ints(minIntNumber, maxIntNumber - 1).distinct().limit(numberOfNodes).toArray();
		int[] randomY = randomGenerator.ints(minIntNumber, maxIntNumber - 1).distinct().limit(numberOfNodes).toArray();
		for (int countRandom = 0; countRandom < numberOfNodes; countRandom++) {
			randomPoints[countRandom] = new Node(randomX[countRandom], randomY[countRandom]);
		}
		return randomPoints;
	}
	
	
	/**
	 * @param numberOfNodes
	 * @param min
	 * @param max
	 * @return
	 */
	public Node[] getRandomNodes(int numberOfNodes, int min , int max) {
		Node[] randomPoints = new Node[numberOfNodes];
		Random randomGenerator = new Random();

		int[] randomX = randomGenerator.ints(min, max - 1).distinct().limit(numberOfNodes).toArray();
		int[] randomY = randomGenerator.ints(min, max - 1).distinct().limit(numberOfNodes).toArray();
		for (int countRandom = 0; countRandom < numberOfNodes; countRandom++) {
			randomPoints[countRandom] = new Node(randomX[countRandom], randomY[countRandom]);
		}
		return randomPoints;
	}

	/**
	 * Method that creates the wanted data structures for each different number of
	 * Nodes
	 * 
	 * @throws IOException
	 */
	public void doTest() throws IOException {
		int num_of_nodes; // the number of nodes that will be inserted into the data structure on each run
		int nodes_to_add;
		long comparisonsA1=0; 
		long failComparisonsA1=0;
		long comparisonsA2=0;
		long failComparisonsA2 = 0;
		long accessesB1= 0 ;
		long failAccessesB1 = 0;
		long accessesB2 = 0; 
		long failAccessesB2 = 0;

		
		Node[] totalRandomNodes = this.getRandomNodes(100000);   	// an array containing 100.000 random nodes in total.
		Node[] nodesToSearch = new Node[numOfSearches];
		Node[] failNodes2 = new Node[numOfSearches]; 

		
		// our data structures:
		List tester_list = new List();	//A1
		Hash tester_hash = new Hash();	//A2
		DiskList tester_diskList = new DiskList();	//B1
		DiskHash tester_diskHash = new DiskHash();	//B2
		

		File file0 = new File("../file_forB1.bin");
		RandomAccessFile fileB1 = new RandomAccessFile(file0, "rw");

		File file1 = new File("../file_forB2.bin");
		RandomAccessFile fileB2 = new RandomAccessFile(file1, "rw");
		
		// for each numOfNodes, we create the data structures
		for (int i = 0; i < nodesToAdd.length; i++) {
			comparisonsA1=0; 
			failComparisonsA1=0;
			comparisonsA2=0;
			failComparisonsA2 = 0;
			accessesB1= 0 ;
			failAccessesB1 = 0;
			accessesB2 =0 ; 
			failAccessesB2=0; 
			
			num_of_nodes = nodesPerTest[i];
			nodes_to_add = nodesToAdd[i];
			
			Node[] nodesPerTest = new Node[num_of_nodes];
			System.arraycopy(totalRandomNodes, 0, nodesPerTest, 0, num_of_nodes);

			// nodes to be added in the data structures...
			Node[] extraNodes = new Node[nodes_to_add];
			System.arraycopy(totalRandomNodes, num_of_nodes - nodes_to_add, extraNodes, 0, nodes_to_add);


			// randomly copying #numOfSearches nodes to use them later for successful searching
			Random randomizer = new Random();

			int[] randomIndexes = randomizer.ints(0, nodesPerTest.length - 1).distinct().limit(numOfSearches).toArray();

			for (int k = 0; k < randomIndexes.length; k++) {
				nodesToSearch[k] = nodesPerTest[randomIndexes[k]];
			}
			
		
			//node array containing nodes used for failure searches in hashTables 
			failNodes2 = this.getRandomNodes(numOfSearches, minIntNumber, maxIntNumber+numOfSearches);
			//System.arraycopy(totalRandomNodes, num_of_nodes , failNodes2, 0, numOfSearches);
			
			
			System.err.println("->Starting test for " + num_of_nodes + " nodes");
			
			/*************************MEM LIST*******************************/
			// adding random nodes in (memory) linked list
			for (int k = 0; k < num_of_nodes; k++) {
				tester_list.add(nodesPerTest[k]);
			}
			
			// doing #numOfSearches successful searches in (memory) linked list 
			for( int k=0; k < numOfSearches; k++) {
				comparisonsA1 = tester_list.search(nodesToSearch[k]) + comparisonsA1; 
			}

			meanComparisonsA1[i]= (double) comparisonsA1 / numOfSearches ; 
			
			// doing #numOfSearches unsuccessful searches in (memory) linked list 
			for( int k=0; k < numOfSearches; k++) {
				failComparisonsA1 = tester_list.search(failNodes2[k]) + failComparisonsA1; 
			}
			meanFailComparisonsA1[i] = (double) failComparisonsA1 / numOfSearches; 
			
			
			
			/*************************MEM HASH TABLE*******************************/
			// adding random nodes in (memory) hashTable
			for (int k = 0; k < num_of_nodes; k++) {
				tester_hash.add(nodesPerTest[k]);
			}
			
			// doing #numOfSearches searches in (memory) hashTable
			for (int k = 0; k < numOfSearches; k++) {
				comparisonsA2 =tester_hash.search(nodesToSearch[k]) + comparisonsA2;
			}
			meanComparisonsA2[i] = (double) comparisonsA2 / numOfSearches ;

			
			// doing #numOfSearches unsuccessful searches in (memory) linked list 
			for( int k=0; k < numOfSearches; k++) {
				failComparisonsA2 = tester_hash.search(failNodes2[k]) + failComparisonsA2; 
			}
			meanFailComparisonsA2[i] = (double) failComparisonsA2 / numOfSearches;
			
			
			
			/*************************DISK LIST*******************************/
			// growing the random diskList using #nodes_to_add Nodes from extraNodes array
			for (int k = 0; k < nodes_to_add; k++) {
				tester_diskList.addNode(extraNodes[k], fileB1);
			}
			
			// doing #numOfSearches searches
			for (int k = 0; k < numOfSearches; k++) {
				accessesB1 = tester_diskList.searchDiskList(nodesToSearch[k], fileB1) + accessesB1;
			}
			meanAccessesB1[i] = (double) accessesB1 / numOfSearches;
			
			
			// doing #numOfSearches unsuccessful searches in (memory) linked list 
			for( int k=0; k < numOfSearches; k++) {
				failAccessesB1 = tester_diskList.searchDiskList(failNodes2[k], fileB1) + failAccessesB1; 	
			}
			meanFailAccessesB1[i] = (double) failAccessesB1 / numOfSearches;
			
			
			
			/*************************DISK HASH TABLE*******************************/
			// adding random nodes in (disk) hashTable
			for (int k = 0; k < nodes_to_add; k++) {
				tester_diskHash.addNode2(extraNodes[k],fileB2);
			}
			
			// doing #numOfSearches searches
			for( int k=0; k < numOfSearches; k++) {
				accessesB2 = tester_diskHash.searchDiskHash(nodesToSearch[k], fileB2) + accessesB2;
			}
			meanAccessesB2[i] = (double)  accessesB2 / numOfSearches;
		
			
			//doing #numOfSearches searches
			for( int k=0; k < numOfSearches; k++) {
				failAccessesB2 = tester_diskHash.searchDiskHash(failNodes2[k], fileB2) + failAccessesB2;
			}
			meanFailAccessesB2[i] = (double)  failAccessesB2 / numOfSearches;
	
			
		} // end of for loop 
		fileB1.close();
		file0.delete();
		fileB2.close();
		file1.delete();	
	}
	
	//delete file if it exists
		public void deleteFile(String fileName) {
			Path fileToDeletePath = Paths.get(fileName);
			try {
				Files.delete(fileToDeletePath);
			} 
			catch (Exception e) {
				System.out.println("file '" +  fileName + "' created.");
			}
		}

	
	/**
	 * (memory list)
	 * method to print number of mean comparisons while searching for 100 nodes out of numOfNodes per test 
	 */
	public void meanComparisonsA1() {
		for(int i=0; i<nodesPerTest.length; i++) {
			System.err.println("Mean comparisons for searching "+numOfSearches+" nodes out of "+nodesPerTest[i]+" nodes in memory list: "+ meanComparisonsA1[i]);
		}
	}
	
	/**
	 * (memory list)
	 * method to print number of mean comparisons while searching for 100 nodes THAT DONT EXIST IN DATA STRUCTRE out of numOfNodes per test 
	 */
	public void failComparisonsA1() {
		for(int i=0; i<nodesPerTest.length; i++) {
			System.err.println("Mean comparisons for "+numOfSearches+" faillure searches out of "+nodesPerTest[i]+" nodes in memory list: "+ meanFailComparisonsA1[i]);
		}
	}
	
	
	/**
	 * (memory hash table)
	 * method to print number of mean comparisons while searching for 100 nodes out of numOfNodes per test 
	 */
	public void meanComparisonsA2() {
		for(int i=0; i<nodesPerTest.length; i++) {
			System.err.println("Mean comparisons for searching "+numOfSearches+" nodes out of "+nodesPerTest[i]+" nodes in memory hash Table: "+ meanComparisonsA2[i]);
		}
	}
	
	/**
	 * (memory hash table)
	 * method to print number of mean comparisons while searching for 100 nodes THAT DONT EXIST IN DATA STRUCTRE out of numOfNodes per test 
	 */
	public void failComparisonsA2() {
		for(int i=0; i<nodesPerTest.length; i++) {
			System.err.println("Mean comparisons for "+numOfSearches+" faillure searches out of "+nodesPerTest[i]+" nodes in memory hash Table: "+ meanFailComparisonsA2[i]);
		}
	}
	
	/**
	 * (disk list)
	 * method to print number of mean disk accesses while searching for 100 nodes out of numOfNodes per test 
	 */
	public void printMeanAccessesB1() {
		for(int i=0; i<nodesPerTest.length; i++) {
			int pages = nodesPerTest[i] * 8 / 256 + 1;
			System.err.println("Mean disk accesses for searching "+numOfSearches+" nodes out of "+nodesPerTest[i]+" nodes ("+pages+" data pages) in disk list: "+meanAccessesB1[i]);
		}
	}
	
	/**
	 * (disk list)
	 * method to print number of mean disk accesses while searching for 100 nodes THAT DONT EXIST IN DATA STRUCTRE out of numOfNodes per test 
	 */
	public void printFailAccessesB1() {
		for(int i=0; i<nodesPerTest.length; i++) {
			int pages = nodesPerTest[i] * 8 / 256 + 1;
			System.err.println("Mean disk accesses for "+numOfSearches+" faillure searches out of "+nodesPerTest[i]+" nodes ("+pages+" data pages) in disk list: "+meanFailAccessesB1[i]);
		}
	}
	
	/**
	 * (disk hash)
	 * method to print number of mean disk accesses while searching for 100 nodes out of numOfNodes per test 
	 */
	public void printMeanAccessesB2() {
		for(int i=0; i<nodesPerTest.length; i++) {
			int pages = nodesPerTest[i] * 8 / 256 +1;
			System.err.println("Mean disk accesses for searching "+numOfSearches+" nodes out of "+nodesPerTest[i]+" nodes ("+pages+" data pages) in disk hash table: "+meanAccessesB2[i]);
		}
	}
	
	
	/**
	 * (disk hash)
	 * method to print number of mean disk accesses while searching for 100 nodes out of numOfNodes per test 
	 */
	public void printFailAccessesB2() {
		for(int i=0; i<nodesPerTest.length; i++) {
			int pages = nodesPerTest[i] * 8 / 256 +1;
			System.err.println("Mean disk accesses for searching "+numOfSearches+" faillure searches out of "+nodesPerTest[i]+" nodes ("+pages+" data pages) in disk hash table: "+meanFailAccessesB2[i]);
		}
	}

} // END OF CLASS
