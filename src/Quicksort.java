import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * {Project Description Here}
 */

/**
 * The class containing the main method.
 *
 * @author {Ibrahim Khalilov} {Francisca Wood}
 * @version {ibrahimk} {franciscawood}
 */

// On my honor:
//
// - I have not used source code obtained from another student,
// or any other unauthorized source, either modified or
// unmodified.
//
// - All source code and documentation used in my program is
// either my original work, or was derived by me from the
// source code published in the textbook for this course.
//
// - I have not discussed coding details about this project with
// anyone other than my partner (in the case of a joint
// submission), instructor, ACM/UPE tutors or the TAs assigned
// to this course. I understand that I may discuss the concepts
// of this program with other students, and that another student
// may help me debug my program so long as neither of us writes
// anything during the discussion or modifies any computer file
// during the discussion. I have violated neither the spirit nor
// letter of this restriction.

public class Quicksort {

	/**
	 * @param args Command line parameters. See the project spec!!!
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		int bufferCount = Integer.parseInt(args[1]);
		RandomAccessFile fileToSort = null;
		try {
			fileToSort = new RandomAccessFile(args[0], "rw");
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (fileToSort != null) {
			FileWriter statistics = new FileWriter(args[2], true);
			// starts sorting on file with first argument name
			LRUBufferPool bufPool = new LRUBufferPool(fileToSort, bufferCount);
			bufPool.closeFile();

			statistics.write("Sort on " + args[0] + "\n");
			statistics.write("Cache Hits: " + bufPool.getHits() + "\n");
			statistics.write("Disk reads: " + bufPool.getReads() + "\n");
			statistics.write("Disk writes: " + bufPool.getWrites() + "\n");
			statistics.write("Time is " + bufPool.getTime() + "\n");
			statistics.flush();
			statistics.close();
		} else {
			System.out.println("No such file with name: " + args[0]);
		}
	}
}
