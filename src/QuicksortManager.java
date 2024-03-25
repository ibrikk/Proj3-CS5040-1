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


    private short choosePivot(int leftIndex, int rightIndex)
        throws IOException {
        short firstKey = bufferPoolInstance.fetchKey(leftIndex);
        short middleKey = bufferPoolInstance.fetchKey((leftIndex + rightIndex)
            / 2);
        short lastKey = bufferPoolInstance.fetchKey(rightIndex);
        if ((firstKey - middleKey) * (lastKey - firstKey) >= 0) {
            return firstKey;
        }
        else if ((middleKey - firstKey) * (lastKey - middleKey) >= 0) {
            return middleKey;
        }
        else {
            return lastKey;
        }
    }


    private void insertionSort(int leftIndex, int rightIndex)
        throws IOException {
        for (int i = leftIndex + 1; i <= rightIndex; i++) {
            short currentKey = bufferPoolInstance.fetchKey(i);
            int j = i;
            // Move elements greater than currentKey to one position ahead of
            // their current position
            while (j > leftIndex && bufferPoolInstance.fetchKey(j
                - 1) > currentKey) {
                swapElements(j, j - 1);
                j--;
            }
        }
    }


    private int partition(int leftIndex, int rightIndex) throws IOException {
        // Choose a pivot using the improved method to select a median value
        short pivotValue = choosePivot(leftIndex, rightIndex);

        int i = leftIndex;
        int j = rightIndex;

        // Continue looping until the indices meet
        while (i <= j) {
            // Increment i until an element greater than the pivot is found
            while (bufferPoolInstance.fetchKey(i) < pivotValue) {
                i++;
            }

            // Decrement j until an element less than the pivot is found
            while (bufferPoolInstance.fetchKey(j) > pivotValue) {
                j--;
            }

            // If i is less than or equal to j, swap the elements and move the
            // indices
            if (i <= j) {
                swapElements(i, j);
                i++;
                j--;
            }
        }
        // Return the partition point
        return i;
    }


    private void performQuickSort(int leftIndex, int rightIndex)
        throws IOException {
        if (leftIndex < rightIndex) {
            int difference = rightIndex - leftIndex + 1;
            if (difference >= 3 && difference <= 9) {
                insertionSort(leftIndex, rightIndex);
            }
            else {
                int pivotIndex = partition(leftIndex, rightIndex);
                performQuickSort(leftIndex, pivotIndex - 1);
                performQuickSort(pivotIndex + 1, rightIndex);
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
