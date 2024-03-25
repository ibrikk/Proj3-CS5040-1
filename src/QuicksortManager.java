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


    private short choosePivot(int left, int right) throws IOException {
        int middle = (left + right) / 2;
        short leftKey = bufferPoolInstance.fetchKey(left);
        short middleKey = bufferPoolInstance.fetchKey(middle);
        short rightKey = bufferPoolInstance.fetchKey(right);

        if ((leftKey - middleKey) * (rightKey - leftKey) >= 0)
            return leftKey;
        else if ((middleKey - leftKey) * (rightKey - middleKey) >= 0)
            return middleKey;
        else
            return rightKey;
    }


    private void performQuickSort(int leftIndex, int rightIndex)
        throws IOException {
        if (rightIndex <= leftIndex) {
            return;
        }
        int leftTemp = leftIndex;
        int rightTemp = rightIndex;
        short pivotValue = choosePivot(leftIndex, rightIndex);
        int currentPosition = leftIndex;
        while (currentPosition <= rightTemp) {
            short key = bufferPoolInstance.fetchKey(currentPosition);
            if (key < pivotValue) {
                swapElements(leftTemp++, currentPosition++);
            }
            else if (key > pivotValue) {
                swapElements(currentPosition, rightTemp--);
            }
            else {
                currentPosition++;
            }
        }
        performQuickSort(leftIndex, leftTemp - 1);
        performQuickSort(rightTemp + 1, rightIndex);
    }


    private void swapElements(int firstPosition, int secondPosition)
        throws IOException {
        if (firstPosition != secondPosition) {
            byte[] tempForFirst = new byte[SIZE_OF_RECORD];
            byte[] tempForSecond = new byte[SIZE_OF_RECORD];

            bufferPoolInstance.retrieveBytes(tempForFirst, SIZE_OF_RECORD,
                firstPosition);
            bufferPoolInstance.retrieveBytes(tempForSecond, SIZE_OF_RECORD,
                secondPosition);

            // Compare bytes before swapping to avoid unnecessary operation
            if (!Arrays.equals(tempForFirst, tempForSecond)) {
                bufferPoolInstance.storeBytes(tempForFirst, SIZE_OF_RECORD,
                    secondPosition);
                bufferPoolInstance.storeBytes(tempForSecond, SIZE_OF_RECORD,
                    firstPosition);
            }
        }
    }
}
