import java.io.IOException;

public class QuicksortManager {
    public static LRUBufferPool bufferPool;
    private static final int RECORD_SIZE = 4;

    public QuicksortManager(LRUBufferPool pool, int filelength)
        throws IOException {
        bufferPool = pool;
        quicksort(0, (filelength / RECORD_SIZE) - 1);
    }


    private void quicksort(int i, int j) throws IOException {
        if (j <= i) {
            return;
        }
        int iTemp = i;
        int jTemp = j;
        short v = bufferPool.retrieveKey(i);
        int count = i;
        while (count <= jTemp) {
            if (bufferPool.retrieveKey(count) < v) {
                swap(iTemp++, count++);
            }
            else if (bufferPool.retrieveKey(count) > v) {
                swap(count, jTemp--);
            }
            else {
                count++;
            }
        }
        quicksort(i, iTemp - 1);
        quicksort(jTemp + 1, j);
    }


    private void swap(int l, int r) throws IOException {
        byte[] lTemp = new byte[RECORD_SIZE];
        byte[] rTemp = new byte[RECORD_SIZE];
        bufferPool.getBytes(lTemp, RECORD_SIZE, l);
        bufferPool.getBytes(rTemp, RECORD_SIZE, r);
        bufferPool.insert(lTemp, RECORD_SIZE, r);
        bufferPool.insert(rTemp, RECORD_SIZE, l);
    }
}
