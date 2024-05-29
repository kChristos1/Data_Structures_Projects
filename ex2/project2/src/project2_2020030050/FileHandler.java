package project2_2020030050;

//A class that reads the integers from the input files. It returns the integers in arrays.

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class FileHandler {

	// class variables
	private RandomAccessFile file;
	private File file0;

	// constructor
	public FileHandler(String str) throws FileNotFoundException {
		this.file0 = new File(str);
		this.file = new RandomAccessFile(file0, "r");
	}

	// methods
	public int[] get_integers() throws IOException {
		long file_length = this.file.length();
		int[] integers = new int[(int) file_length];
		int i = 0; // index for integers array

		long file_pointer = 0;
		file.seek(file_pointer); // seek to the beginning of the file

		while (file.getFilePointer() != file_length) {
			integers[i] = file.readInt();
			i++;
		}

		return integers; // will return an empty array of integers if while won't execute
	}

}
