import java.io.IOException;
import java.util.Arrays;

public class QuicksortManager {
	public static LRUBufferPool bufferPoolInstance;
	private static final int SIZE_OF_RECORD = 4;
	private static final int INSERTION_SORT_THRESHOLD = 3;

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
		if (rightIndex <= leftIndex) {
			return;
		} else if (rightIndex - leftIndex + 1 < INSERTION_SORT_THRESHOLD) {
			insertionSort(leftIndex, rightIndex);
		} else {
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
		System.out.println("Entering INSERTION SORT");
		System.out.println("This is an array to insert sort:");
		for (int k = rightIndex; k >= leftIndex; k--) {
			short currK = bufferPoolInstance.fetchKey(k);
			System.out.println(currK);
		}
		for (int i = leftIndex + 1; i <= rightIndex; i++) {
			short currentKey = bufferPoolInstance.fetchKey(i);
			for (int j = i - 1; (j >= leftIndex) && (currentKey > bufferPoolInstance.fetchKey(j)); j--) {
				swapElements(j, j + 1);
			}
		}

		System.out.println("This is the sorted ARRAY:");
		for (int k = rightIndex; k >= leftIndex; k--) {
			short currK = bufferPoolInstance.fetchKey(k);
			System.out.println(currK);
		}
	}

	private void swapElements(int firstPosition, int secondPosition) throws IOException {
		byte[] tempForFirst = new byte[SIZE_OF_RECORD];
		byte[] tempForSecond = new byte[SIZE_OF_RECORD];
		bufferPoolInstance.retrieveBytes(tempForFirst, SIZE_OF_RECORD, firstPosition);
		short key1 = bufferPoolInstance.fetchKey(firstPosition);
		bufferPoolInstance.retrieveBytes(tempForSecond, SIZE_OF_RECORD, secondPosition);
		short key2 = bufferPoolInstance.fetchKey(secondPosition);

		if (!Arrays.equals(tempForFirst, tempForSecond)) {
//			System.out.println("Values are not the same");
//			System.out.println(key1);
//			System.out.println(key2);
			bufferPoolInstance.storeBytes(tempForFirst, SIZE_OF_RECORD, secondPosition);
			bufferPoolInstance.storeBytes(tempForSecond, SIZE_OF_RECORD, firstPosition);
		}
	}

}
