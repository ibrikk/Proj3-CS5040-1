import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * {This project develops a comprehensive system designed to enhance data
 * processing efficiency through optimized buffering and sorting mechanisms.
 * Utilizing a Least Recently Used (LRU) caching strategy, the system minimizes
 * disk I/O operations, thereby significantly improving the performance of data
 * retrieval and manipulation tasks. At its core, the project is comprised of
 * several key components}
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

            long startTime = System.currentTimeMillis();

            LRUBufferPool memoryPool = new LRUBufferPool(targetFile, poolSize);
            memoryPool.closeFileStream();

            Statistics.setExecutionTime(System.currentTimeMillis() - startTime);

            logFile.write("Sorting process initiated for: " + arguments[0]
                + "\n");
            logFile.write("Cache Hit Count: " + Statistics.getHits() + "\n");
            logFile.write("Number of Reads from Disk: " + Statistics.getReads()
                + "\n");
            logFile.write("Number of Writes to Disk: " + Statistics.getWrites()
                + "\n");
            logFile.write("Elapsed Time: " + Statistics.measureTime()
                + " milliseconds \n");
            logFile.flush();
            logFile.close();
        }
        else {
            System.out.println("File not found: " + arguments[0]);
        }
    }
}
