public class Statistics {
    public static int hits = 0;
    public static int reads = 0;
    public static int writes = 0;
    public static long executionTime = 0;

    public static long measureTime() {
        return executionTime;
    }


    public static int getHits() {
        return hits;
    }


    public static int getReads() {
        return reads;
    }


    public static int getWrites() {
        return writes;
    }
}
