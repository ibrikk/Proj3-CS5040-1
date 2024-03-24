
public class Statistics {
    static int hits = 0;
    static int reads = 0;
    static int writes = 0;
    static long executionTime;

    public static int getHits() {
        return hits;
    }


    public static int getReads() {
        return reads;
    }


    public static int getWrites() {
        return writes;
    }


    public static long measureTime() {
        return (executionTime / 1000000);
    }
}
