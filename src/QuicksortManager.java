import java.io.IOException;
import java.util.Arrays;

public class QuicksortManager {
	public static LRUBufferPool bufferPoolInstance;
	private static final int SIZE_OF_RECORD = 4;
	private static final int INSERTION_SORT_THRESHOLD = 2;

	public QuicksortManager(LRUBufferPool pool, int lengthOfFile) throws IOException {
		bufferPoolInstance = pool;
		performQuickSort(0, (lengthOfFile / SIZE_OF_RECORD) - 1);
	}

	private short choosePivot(int leftIndex, int rightIndex) throws IOException {
		short firstKey = bufferPoolInstance.fetchKey(leftIndex);
		short middleKey = bufferPoolInstance.fetchKey((leftIndex + rightIndex) / 2);
		short lastKey = bufferPoolInstance.fetchKey(rightIndex);
		if ((firstKey > middleKey) ^ (firstKey > lastKey))
			return firstKey;
		else if ((middleKey > firstKey) ^ (middleKey > lastKey))
			return middleKey;
		else
			return lastKey;
	}

	private void performQuickSort(int leftIndex, int rightIndex) throws IOException {
		if (rightIndex - leftIndex + 1 < INSERTION_SORT_THRESHOLD) {
			insertionSort(leftIndex, rightIndex);
		} else {
			if (rightIndex <= leftIndex) {
				return;
			}
			int leftTemp = leftIndex;
			int rightTemp = rightIndex;
			short pivotValue = choosePivot(leftTemp, rightTemp);
			int currentPosition = leftIndex;
			while (currentPosition <= rightTemp) {
				short key = bufferPoolInstance.fetchKey(currentPosition);
				if (key < pivotValue) {
					swapElements(leftTemp++, currentPosition++);
				} else if (key > pivotValue) {
					swapElements(currentPosition, rightTemp--);
				} else {
					currentPosition++;
				}
			}
			performQuickSort(leftIndex, leftTemp - 1);
			performQuickSort(rightTemp + 1, rightIndex);
		}
	}
	
    private void insertionSort(int leftIndex, int rightIndex) throws IOException {
        for (int i = leftIndex + 1; i <= rightIndex; i++) {
            short currentKey = bufferPoolInstance.fetchKey(i);
            int j = i - 1;
            while (j >= leftIndex && bufferPoolInstance.fetchKey(j) > currentKey) {
                swapElements(j,j+1);
                j--;
            }
        }
    }

	private void swapElements(int firstPosition, int secondPosition) throws IOException {
		byte[] tempForFirst = new byte[SIZE_OF_RECORD];
		byte[] tempForSecond = new byte[SIZE_OF_RECORD];
		bufferPoolInstance.retrieveBytes(tempForFirst, SIZE_OF_RECORD, firstPosition);
		bufferPoolInstance.retrieveBytes(tempForSecond, SIZE_OF_RECORD, secondPosition);
		if (!Arrays.equals(tempForFirst, tempForSecond)) {
			bufferPoolInstance.storeBytes(tempForFirst, SIZE_OF_RECORD, secondPosition);
			bufferPoolInstance.storeBytes(tempForSecond, SIZE_OF_RECORD, firstPosition);
		}
	}
}
