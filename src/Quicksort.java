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
     * Main method to execute the sorting process.
     * 
     * @param arguments
     *            Command line arguments provided to the program.
     * @throws IOException
     *             If an I/O error occurs.
     */
    public static void main(String[] arguments) throws IOException {
        int poolSize = Integer.parseInt(arguments[1]);
        RandomAccessFile targetFile = null;
        try {
            targetFile = new RandomAccessFile(arguments[0], "rw");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        if (targetFile != null) {
            FileWriter logFile = new FileWriter(arguments[2], true);
            LRUBufferPool memoryPool = new LRUBufferPool(targetFile, poolSize);
            memoryPool.closeFileStream();

            logFile.write("Sorting process initiated for: " + arguments[0]
                + "\n");
            logFile.write("Cache Hit Count: " + memoryPool.getHits() + "\n");
            logFile.write("Number of Reads from Disk: " + memoryPool.getReads()
                + "\n");
            logFile.write("Number of Writes to Disk: " + memoryPool.getWrites()
                + "\n");
            logFile.write("Elapsed Time: " + memoryPool.measureTime() + "\n");
            logFile.flush();
            logFile.close();
        }
        else {
            System.out.println("File not found: " + arguments[0]);
        }
    }
}
