package org.tuc.hash;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;

import org.tuc.Node;
import org.tuc.PagePointer;
import org.tuc.counter.MultiCounter;
import org.tuc.list.DiskList;
import org.tuc.list.List;
import org.tuc.test.MainClass;

/**
 * @author Christos Kostadimas
 * 
 *         I consider a page full when it reaches size = 256-8... basically a
 *         page can fit 31 nodes and one long integer that contains the overflow
 *         page pointer
 *
 */
public class DiskHash {

	private PagePointer[] pagePtr;
	private int pageSize;
	private byte[] dataPage;
	private int M;
	private byte[] xBuf;
	private byte[] yBuf;
	private byte[] pair;
	private long overflow;
	byte[] overflowBuf;
	long ktr = 0;

	// constructor of class
	public DiskHash() {
		this.pageSize = MainClass.DATA_PAGE_SIZE;
		this.M = MainClass.M;
		this.pagePtr = new PagePointer[M];
		this.xBuf = new byte[4]; // 1 integer = 4 bytes
		this.yBuf = new byte[4]; // 1 integer = 4 bytes
		this.pair = new byte[8]; // 2 integers = 8 bytes
		this.overflowBuf = new byte[8];
		this.overflow = 0;
	}

	// class methods:
	/**
	 * Method for adding a node in disk hashTable. First thing to do is to read the
	 * last dataPage of the chain. The address of this page is given from the
	 * pagePtr[pos] of the pagePtr array. This page is transfered into the main
	 * memory in a dataPage byte array. If the page is not full then node is
	 * appended to the page and the page will be re-written in the place where it
	 * was read. If the page is full then an overflow page is added to the chain and
	 * the overflow page will be written to the end of the file.
	 * 
	 * @param node
	 * @param myFile
	 * @throws IOException
	 */
	public void addNode2(Node node, RandomAccessFile myFile) throws IOException {
		// initialization of variables
		dataPage = new byte[pageSize];
		dataPage = this.initDataPage(dataPage);
		long lastPage = 0;
		int pageIndex = 0;
		boolean full = false;

		int x = node.getX();
		int y = node.getY();

		// turning node (x,y) to a byte[] pair with lenght=8
		xBuf = ByteBuffer.allocate(4).putInt(x).array();
		yBuf = ByteBuffer.allocate(4).putInt(y).array();

		pair = DiskList.concatByteArrays(xBuf, yBuf);

		int pos = Hash.hashFunc(x, y);

		if (pagePtr[pos] != null) {
			lastPage = pagePtr[pos].getLastPage(); // last page of chain
			// seek to last dataPage of CHAIN
			myFile.seek(lastPage);
			// read last page of chain
			myFile.read(dataPage);

			pageIndex = this.getPageIndex(dataPage);

			if (pageIndex >= pageSize - 8) {
				full = true;
			} else {
				full = false;
			}

			// adding.....
			if (full == true) {
				// adding new page in the chain by writing overflow page's address to the page i
				// JUST read...
				overflow = myFile.length();
				overflowBuf = ByteBuffer.allocate(8).putLong(overflow).array();

				pageIndex = pageSize - 8;

				for (int k = 0; k < 8; k++) {
					dataPage[pageIndex] = overflowBuf[k];
					pageIndex++;
				}
				myFile.seek(lastPage);
				myFile.write(dataPage);

				// creating a new overflow dataPage
				dataPage = new byte[pageSize];
				dataPage = this.initDataPage(dataPage);

				pageIndex = this.getPageIndex(dataPage);

				for (int k = 0; k < 8; k++) {
					dataPage[pageIndex] = pair[k];
					pageIndex++;
				}

				// update last page of chain
				pagePtr[pos].setLastPage(myFile.length());

				// append the overflow page in end of file
				myFile.seek(myFile.length());
				myFile.write(dataPage);

			} else {
				// page not full, add new node x,y to it
				pageIndex = this.getPageIndex(dataPage);
				for (int k = 0; k < 8; k++) {
					dataPage[pageIndex] = pair[k];
					pageIndex++;
				}
				// update last page of chain ...
				pagePtr[pos].setLastPage(lastPage);

				// and place page back from where i read it before
				myFile.seek(lastPage);
				myFile.write(dataPage);
			}

		} else {
			// pagePtr[pos]==null => start a chain...
			pageIndex = 0;
			pagePtr[pos] = new PagePointer(myFile.length(), myFile.length());

			byte[] dataPage2 = new byte[pageSize];
			dataPage2 = this.initDataPage(dataPage2);
			pageIndex = this.getPageIndex(dataPage2);

			for (int k = 0; k < 8; k++) {
				dataPage2[pageIndex] = pair[k];
				pageIndex++;
			}

			myFile.seek(myFile.length());
			myFile.write(dataPage2);

		}

	}

	/**
	 * Method used for searching keyNode in the disk hashTable data structure. The
	 * address of the first page of the chain is given by the pagePtr[pos] of the
	 * pagePtr array. Then the first page is being transfered in a dataPage buffer.
	 * If the keyNode does not exist in this page then the method starts reading all
	 * pages of the chain one by one until the keyNode is found in one of them.If
	 * the keyNode does not exist at all in the chain then all pages of chain will
	 * be read until reaching the last one. Each "read" instruction counts as a disk
	 * access => MultiCounter is being increased by one.
	 * 
	 * @param keyNode
	 * @param myFile
	 * @return number of disk accesses needed to find keyNode in myFile's hashTable
	 * @throws IOException
	 */
	public long searchDiskHash(Node keyNode, RandomAccessFile myFile) throws IOException {
		byte[] dataPage = new byte[pageSize];
		long accesses = 0;
		int x = keyNode.getX();
		int y = keyNode.getY();
		long next_page = 0;

		int pos = Hash.hashFunc(x, y);
		byte[] xBuf = ByteBuffer.allocate(4).putInt(x).array();
		byte[] yBuf = ByteBuffer.allocate(4).putInt(y).array();

		MultiCounter.resetCounter(5);

		byte[] searchPair = DiskList.concatByteArrays(xBuf, yBuf);

		if (pagePtr[pos] != null) {
			long firstChainPage = pagePtr[pos].getFirstPage();
			/*
			 * first thing to do when looking for a node is to see if it exists in the first
			 * dataPage of its chain
			 */
			myFile.seek(firstChainPage);
			myFile.read(dataPage);
			MultiCounter.increaseCounter(5);
			accesses = MultiCounter.getCount(5);

			// checking to see if element exists in first page of chain
			if (this.findPair(dataPage, searchPair) == true) {
				return accesses;
			}

			// if not we traverse the chain..
			while (true) {
				myFile.seek(myFile.getFilePointer() - 8);
				next_page = myFile.readLong();
				// we know when next_page < 0 that we reach the last page , since the last 8
				// bytes=-1 (no overflow page written there...)
				if (next_page >= 0 && next_page <= myFile.length() - pageSize) {
					myFile.seek(next_page);
					myFile.read(dataPage); // updates file pointer
					MultiCounter.increaseCounter(5);
					accesses = MultiCounter.getCount(5);
					// search this dataPage for wanted pair
					if (this.findPair(dataPage, searchPair) == true)
						return accesses;
				} else { // if nextPage < 0 ,that means we reached the last page of chain.. last 8 spots
							// of page = -1
					break;
				}

			}
			return accesses;
		} else {
			// pagePtr[pos]==null, there's nothing to search here...
			MultiCounter.resetCounter(5);
			return 0;

		}
	}

	/**
	 * Method to check if searchPair exists within dataPage array
	 * (searchPair.length=8bytes and dataPage.length=256bytes)
	 * 
	 * @param dataPage
	 * @param searchPair
	 * @return true if element has been found, else false
	 */
	public boolean findPair(byte[] dataPage, byte[] searchPair) {
		for (int i = 0; i < pageSize; i = i + 8) {
			if (dataPage[i] == searchPair[0] && dataPage[i + 1] == searchPair[1] && dataPage[i + 2] == searchPair[2]
					&& dataPage[i + 3] == searchPair[3] && dataPage[i + 4] == searchPair[4]
					&& dataPage[i + 5] == searchPair[5] && dataPage[i + 6] == searchPair[6]
					&& dataPage[i + 7] == searchPair[7]) {
				// element found
				return true; // method stops running if element has been successfully
								// located
			}
		}
		return false;
	}

	/**
	 * Used for page initialization
	 * 
	 * @param dataPage
	 * @return array argument dataPage filled with -1
	 */
	public byte[] initDataPage(byte[] dataPage) {
		dataPage = new byte[pageSize]; // N E W
		for (int k = 0; k < pageSize; k++) {
			dataPage[k] = -1;
		}
		return dataPage;
	}

	/**
	 * @param dataPage
	 * @return index of first empty (=-1) spot of dataPage array
	 */
	public int getPageIndex(byte[] dataPage) {
		int pageIndex = 0;
		for (int i = 0; i < pageSize; i++) {
			if (dataPage[i] == -1) {
				break;
			}
			pageIndex++;
		}
		return pageIndex;
	}

	// setters and getters

	public PagePointer[] getPagePtr() {
		return pagePtr;
	}

	public void setPagePtr(PagePointer[] pagePtr) {
		this.pagePtr = pagePtr;
	}

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

	public int getM() {
		return M;
	}

	public void setM(int m) {
		M = m;
	}

	public byte[] getxBuf() {
		return xBuf;
	}

	public void setxBuf(byte[] xBuf) {
		this.xBuf = xBuf;
	}

	public byte[] getyBuf() {
		return yBuf;
	}

	public void setyBuf(byte[] yBuf) {
		this.yBuf = yBuf;
	}

	public byte[] getPair() {
		return pair;
	}

	public void setPair(byte[] pair) {
		this.pair = pair;
	}

	public long getOverflow() {
		return overflow;
	}

	public void setOverflow(long overflow) {
		this.overflow = overflow;
	}

	public byte[] getOverflowBuf() {
		return overflowBuf;
	}

	public void setOverflowBuf(byte[] overflowBuf) {
		this.overflowBuf = overflowBuf;
	}

	public long getKtr() {
		return ktr;
	}

	public void setKtr(long ktr) {
		this.ktr = ktr;
	}
}