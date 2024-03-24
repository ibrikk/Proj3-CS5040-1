import java.io.IOException;

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
        if (rightIndex <= leftIndex) {
            return;
        }
        int leftTemp = leftIndex;
        int rightTemp = rightIndex;
        short pivotValue = bufferPoolInstance.fetchKey(leftIndex);
        int currentPosition = leftIndex;
        while (currentPosition <= rightTemp) {
            if (bufferPoolInstance.fetchKey(currentPosition) < pivotValue) {
                swapElements(leftTemp++, currentPosition++);
            }
            else if (bufferPoolInstance.fetchKey(
                currentPosition) > pivotValue) {
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
        byte[] tempForFirst = new byte[SIZE_OF_RECORD];
        byte[] tempForSecond = new byte[SIZE_OF_RECORD];
        bufferPoolInstance.retrieveBytes(tempForFirst, SIZE_OF_RECORD,
            firstPosition);
        bufferPoolInstance.retrieveBytes(tempForSecond, SIZE_OF_RECORD,
            secondPosition);
        bufferPoolInstance.storeBytes(tempForFirst, SIZE_OF_RECORD,
            secondPosition);
        bufferPoolInstance.storeBytes(tempForSecond, SIZE_OF_RECORD,
            firstPosition);
    }
}
