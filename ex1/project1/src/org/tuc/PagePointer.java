package org.tuc;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * @author Christos Kostadimas
 * 
 * this class represents each element of the DiskHash table. Each element in position "pos" points
 * -> at the first address of the first data page that contains elements of list/chain
 * -> at the last page -the overflow page- of the chain in this position 
 *
 */
public class PagePointer {
	
	private long firstPage; 
	private long lastPage;
	
	
	public PagePointer(long firstPage , long lastPage) {
		this.firstPage = firstPage; 
		this.lastPage = lastPage;   //last page of overflow pages chain 
		
	}
	
	//null constructor 
	public PagePointer() {
		
	}
	
	//setters and getters 
	public long getFirstPage() {
		return firstPage;
	}

	public void setFirstPage(long firstPage) {
		this.firstPage = firstPage;
	}

	public long getLastPage() {
		return lastPage;
	}

	public void setLastPage(long lastPage) {
		this.lastPage = lastPage;
	}
	
	
	
	
	
		
	
	
}
