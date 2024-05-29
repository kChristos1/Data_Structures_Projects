package org.tuc.list;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.RandomAccessFile;
import java.io.Serializable;
import java.nio.ByteBuffer;

import org.tuc.Node;
import org.tuc.counter.MultiCounter;
import org.tuc.test.MainClass;

/**
 * @author Christos Kostadimas AM 2020030050
 *
 */
public class DiskList {

	// class member variables
	private int pageSize;
	private byte[] dataPage;
	private int pageIndex;
	private int dataX;
	private int dataY;
	private long fileIndex;
	private boolean full;
	private byte[] pair;
	private int counter;

	// class constructor
	public DiskList() {
		this.dataX = 0; // data that will be written within the file in byte form
		this.dataY = 0;
		this.fileIndex = 0;
		this.full = true;
		this.pair = new byte[8]; // 2 integers = 8 bytes
		this.counter = 0;
		this.pageIndex = 0;
		pageSize = MainClass.DATA_PAGE_SIZE;

	}

	// class methods:

	/**
	 * this method whenever a node is given for adding it to file, reads the last
	 * data page of the file "myFile" in a byte buffer with length=256 bytes.
	 * (transfers page from disk to memory) Then if the page is not full , the node
	 * randomNode is being written to that page and the page will be transfered back
	 * in the disk where it was read from. But if the page is full, then a new data
	 * page (array) is created, the node randomNode is being written to that new
	 * page and the page will be transfered back in the disk.
	 * 
	 * @param randomNode
	 * @param myFile
	 * @throws IOException
	 */
	public void addNode(Node randomNode, RandomAccessFile myFile) throws IOException {
		// initialization
		dataPage = new byte[pageSize];
		dataX = randomNode.getX();
		dataY = randomNode.getY();

		// integers dataX and dataY -> byte arrays
		byte[] xBuf = ByteBuffer.allocate(4).putInt(dataX).array();
		byte[] yBuf = ByteBuffer.allocate(4).putInt(dataY).array();
		pair = concatByteArrays(xBuf, yBuf); // length = 8

		// seek to the beginning of last dataPage of the file
		if (myFile.length() < pageSize) {
			fileIndex = 0;
		} else {
			fileIndex = myFile.length() - pageSize;
		}
		myFile.seek(fileIndex);

		// read last dataPage (from disk -> memory)
		myFile.read(dataPage); // file pointer moved pageSize bytes forward

		if (pageIndex >= pageSize) {
			full = true; // page is full
			// changing page => reset pageIndex
			pageIndex = 0;
		} else {
			full = false;
		}

		// if dataPage is full => create new data page , fill it and then write it back
		// in the end of the file
		if (full == true) {
			dataPage = new byte[pageSize];
			// dataPage = this.initDataPage(dataPage);
			pageIndex = 0;

			// fill NEW dataPage with randomNode's x and y
			for (int k = 0; k < 8; k++) {
				dataPage[pageIndex] = pair[k];
				pageIndex++;
			}
			// pair put in dataPage
			// write new dataPage in the END of file !!! (file pointer already pointing
			// there)
			myFile.write(dataPage);
		} else {
			for (int k = 0; k < 8; k++) {
				dataPage[pageIndex] = pair[k];
				pageIndex++;
			}
			// pair put in dataPage

			// seek in the right place (where i read it)
			myFile.seek(fileIndex);

			// re-write dataPage
			myFile.write(dataPage);
		}
	}

	/**
	 * method that concatenates two byte arrays for example: array1={0000, 1111}
	 * array2={0101, 1010} result={0000, 1111, 0101, 1010}
	 * 
	 * @param array1
	 * @param array2
	 * @return concatenation of those arrays
	 */
	// inspiration from:
	// https://www.programiz.com/java-programming/examples/concatenate-two-arrays
	public static byte[] concatByteArrays(byte[] array1, byte[] array2) {
		int aLen = array1.length;
		int bLen = array2.length;
		byte[] result = new byte[aLen + bLen];
		System.arraycopy(array1, 0, result, 0, aLen);
		System.arraycopy(array2, 0, result, aLen, bLen);
		return result;
	}

	/**
	 * method used for searching file myFile to find node keyNode ,returns the number
	 * of disk accesses mad to find element keyNode .
	 * 
	 * @param keyNode
	 * @param myFile
	 * @return number of disk accesses while searching for keyNode in myFile
	 * @throws IOException
	 */
	public long searchDiskList(Node keyNode, RandomAccessFile myFile) throws IOException {
		byte[] dataPage = new byte[pageSize];
		// initialization
		int xKey = keyNode.getX();
		int yKey = keyNode.getY();
		long page_ctr = 0;
		long totalPages = myFile.length() / pageSize;
		long accesses = 0;

		byte[] xBuf = ByteBuffer.allocate(4).putInt(xKey).array();
		byte[] yBuf = ByteBuffer.allocate(4).putInt(yKey).array();

		// node that we are looking for, transformed into a byte array.
		byte[] searchPair = concatByteArrays(xBuf, yBuf);

		// file pointer to beginning of file in order to traverse all elements
		myFile.seek(0);

		MultiCounter.resetCounter(5);
		// reading data pages of file one-by-one until i find the pair x,y = 4bytes +
		// 4bytes = 8 bytes
		while (true) {
			if (page_ctr < totalPages) {
				myFile.read(dataPage); // also updates file pointer
				page_ctr++;
				MultiCounter.increaseCounter(5); // counter for disk accesses (each read() call == 1 disk access)
				accesses = MultiCounter.getCount(5);
				// search the dataPage for wanted pair (per 8 bytes...)
				for (int i = 0; i < pageSize; i = i + 8) {
					if (dataPage[i] == searchPair[0] && dataPage[i + 1] == searchPair[1]
							&& dataPage[i + 2] == searchPair[2] && dataPage[i + 3] == searchPair[3]
							&& dataPage[i + 4] == searchPair[4] && dataPage[i + 5] == searchPair[5]
							&& dataPage[i + 6] == searchPair[6] && dataPage[i + 7] == searchPair[7]) {
						// element found
						return accesses; // method stops running if element has been successfully
											// located
					}
					//
				}
				//
			} else {
				break;
			}
		}
		return accesses;
	}

	// setters and getters

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public byte[] getDataPage() {
		return dataPage;
	}

	public void setDataPage(byte[] dataPage) {
		this.dataPage = dataPage;
	}

	public int getPageIndex() {
		return pageIndex;
	}

	public void setPageIndex(int pageIndex) {
		this.pageIndex = pageIndex;
	}

	public int getDataX() {
		return dataX;
	}

	public void setDataX(int dataX) {
		this.dataX = dataX;
	}

	public int getDataY() {
		return dataY;
	}

	public void setDataY(int dataY) {
		this.dataY = dataY;
	}

	public long getFileIndex() {
		return fileIndex;
	}

	public void setFileIndex(long fileIndex) {
		this.fileIndex = fileIndex;
	}

	public boolean isFull() {
		return full;
	}

	public void setFull(boolean full) {
		this.full = full;
	}

	public byte[] getPair() {
		return pair;
	}

	public void setPair(byte[] pair) {
		this.pair = pair;
	}

	public int getCounter() {
		return counter;
	}

	public void setCounter(int counter) {
		this.counter = counter;
	}

}// END OF CLASS
