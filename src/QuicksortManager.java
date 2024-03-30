import java.io.IOException;
import java.util.Arrays;

public class QuicksortManager {
    public static LRUBufferPool bufferPoolInstance;
    private static final int SIZE_OF_RECORD = 4;
    private static final int INSERTION_SORT_THRESHOLD_MAX = 10;
    private static final int INSERTION_SORT_THRESHOLD_MIN = 3;

    public QuicksortManager(LRUBufferPool pool, int lengthOfFile)
        throws IOException {
        bufferPoolInstance = pool;
        performQuickSortHybrid(0, (lengthOfFile / SIZE_OF_RECORD) - 1);

        insertionSort(0, (lengthOfFile / SIZE_OF_RECORD) - 1);
    }

// private int choosePivotIndex(int leftIndex, int rightIndex)
// throws IOException {
// short firstKey = bufferPoolInstance.fetchKey(leftIndex);
// short middleKey = bufferPoolInstance.fetchKey((leftIndex + rightIndex)
// / 2);
// short lastKey = bufferPoolInstance.fetchKey(rightIndex);
//
// if ((firstKey > middleKey) ^ (firstKey > lastKey))
// return leftIndex;
// else if ((middleKey > firstKey) ^ (middleKey > lastKey))
// return (leftIndex + rightIndex) / 2;
// else
// return rightIndex;
// }


    private int choosePivotIndex(int leftIndex, int rightIndex)
        throws IOException {
        return (leftIndex + rightIndex) / 2;
    }


    private void performQuickSortHybrid(int leftIndex, int rightIndex)
        throws IOException {
        if (rightIndex <= leftIndex) {
            return;
        }
        if (rightIndex - leftIndex + 1 <= INSERTION_SORT_THRESHOLD_MAX
            && rightIndex - leftIndex + 1 >= INSERTION_SORT_THRESHOLD_MIN) {
            // Defer to insertion sort for the final tuning at the end of the
            // method
            return;
        }
        else {
            int pivotIndex = choosePivotIndex(leftIndex, rightIndex);
            swapElements(pivotIndex, rightIndex);
            int partitionIndex = partitionDSA(leftIndex, rightIndex - 1,
                bufferPoolInstance.fetchKey(rightIndex));
            swapElements(partitionIndex, rightIndex);

            performQuickSortHybrid(leftIndex, partitionIndex - 1);
            performQuickSortHybrid(partitionIndex + 1, rightIndex);
        }
    }


    private int partitionDSA(int leftIndex, int rightIndex, short pivot)
        throws IOException {
        int i = leftIndex;
        int j = rightIndex;

        while (true) {

            while (bufferPoolInstance.fetchKey(i) < pivot) {
                i++;
            }

            while (bufferPoolInstance.fetchKey(j) > pivot) {
                j--;
            }
            if (i >= j) {
                return j;
            }
            swapElements(i, j);
            i++;
            j--;
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
                j--;
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
