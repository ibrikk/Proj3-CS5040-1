import java.io.IOException;
import java.util.Arrays;

public class QuicksortManager {
    public static LRUBufferPool bufferPoolInstance;
    private static final int SIZE_OF_RECORD = 4;

    public QuicksortManager(LRUBufferPool pool, int lengthOfFile)
        throws IOException {
        bufferPoolInstance = pool;
        performQuickSort(0, (lengthOfFile / SIZE_OF_RECORD) - 1);
    }


    private void performQuickSort(int leftIndex, int rightIndex)
        throws IOException {
        if (leftIndex < rightIndex) {
            if (rightIndex - leftIndex <= 15) { // Threshold check
                insertionSort(leftIndex, rightIndex);
            }
            else {
                int pivotIndex = partition(leftIndex, rightIndex);
                performQuickSort(leftIndex, pivotIndex - 1);
                performQuickSort(pivotIndex + 1, rightIndex);
            }
        }
    }


    private int partition(int leftIndex, int rightIndex) throws IOException {
        // Fetch the pivot key using the middle record for simplicity
        short pivotValue = bufferPoolInstance.fetchKey((leftIndex + rightIndex)
            / 2);

        // Initialize pointers for the partitioning process
        int i = leftIndex - 1;
        int j = rightIndex + 1;

        // Start the partitioning loop
        while (true) {
            // Move the left pointer (i) to the right as long as the found keys
            // are less than the pivot
            do {
                i++;
            }
            while (bufferPoolInstance.fetchKey(i) < pivotValue);

            // Move the right pointer (j) to the left as long as the found keys
            // are greater than the pivot
            do {
                j--;
            }
            while (bufferPoolInstance.fetchKey(j) > pivotValue);

            // If the pointers meet or cross, the partitioning is complete
            if (i >= j) {
                return j; // j is now the index of the last element in the lower
                          // partition
            }

            // Swap elements across the pivot to ensure proper ordering
            swapElements(i, j);
        }
    }


    private void insertionSort(int leftIndex, int rightIndex)
        throws IOException {
        for (int i = leftIndex + 1; i <= rightIndex; i++) {
            short currentKey = bufferPoolInstance.fetchKey(i);
            int j = i - 1;

            while (j >= leftIndex && bufferPoolInstance.fetchKey(
                j) > currentKey) {
                swapElements(j, j + 1);
                j = j - 1;
            }
        }
    }


    private void swapElements(int firstPosition, int secondPosition)
        throws IOException {
        byte[] tempForFirst = new byte[SIZE_OF_RECORD];
        byte[] tempForSecond = new byte[SIZE_OF_RECORD];
        bufferPoolInstance.retrieveBytes(tempForFirst, SIZE_OF_RECORD,
            firstPosition);
        bufferPoolInstance.retrieveBytes(tempForSecond, SIZE_OF_RECORD,
            secondPosition);
        if (!Arrays.equals(tempForFirst, tempForSecond)) {
            bufferPoolInstance.storeBytes(tempForFirst, SIZE_OF_RECORD,
                secondPosition);
            bufferPoolInstance.storeBytes(tempForSecond, SIZE_OF_RECORD,
                firstPosition);
        }
    }
}
