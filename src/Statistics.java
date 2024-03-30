/**
 * Provides a centralized storage for statistics related to data processing
 * activities, such as caching hits,
 * disk reads, disk writes, and execution time. This class is designed to help
 * monitor and analyze the performance
 * of data buffering and sorting operations, facilitating optimization and
 * debugging efforts.
 */
public class Statistics {
    private static int hits = 0;
    private static int reads = 0;
    private static int writes = 0;
    private static long executionTime = 0;

    /**
     * Retrieves the total execution time recorded.
     *
     * @return The total execution time in milliseconds.
     */
    public static long measureTime() {
        return executionTime;
    }


    /**
     * Retrieves the total number of cache hits recorded.
     *
     * @return The total number of cache hits.
     */
    public static int getHits() {
        return hits;
    }


    /**
     * Increments the total number of cache hits recorded.
     *
     * @return The total number of cache hits.
     */
    public static int incrementHits() {
        return hits++;
    }


    /**
     * Retrieves the total number of disk read operations recorded.
     *
     * @return The total number of disk reads.
     */
    public static int getReads() {
        return reads;
    }


    /**
     * Increments the total number of disk reads recorded.
     *
     * @return The total number of disk reads.
     */
    public static int incrementReads() {
        return reads++;
    }


    /**
     * Retrieves the total number of disk write operations recorded.
     *
     * @return The total number of disk writes.
     */
    public static int getWrites() {
        return writes;
    }


    /**
     * Increments the total number of disk writes recorded.
     *
     * @return The total number of disk writes.
     */
    public static int incrementWrites() {
        return writes++;
    }


    /**
     * Sets the total number of disk write operations recorded.
     *
     * @return The total number of disk writes.
     */
    public static void setExecutionTime(long time) {
        executionTime = time;
    }
}
