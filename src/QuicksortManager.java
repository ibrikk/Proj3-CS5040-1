import java.io.IOException;
import java.util.Arrays;

/**
 * A manager class that implements a hybrid sorting algorithm combining
 * Quicksort and Insertion Sort.
 * This class is designed to sort a large dataset efficiently by leveraging the
 * strengths of both sorting algorithms.
 * Quicksort is used for sorting large segments of the dataset, and Insertion
 * Sort is applied for final tuning
 * on smaller segments to improve the overall sorting performance.
 */
/**
 * @author {Ibrahim Khalilov} {Francisca Wood}
 * @version {ibrahimk} {fransciscawood}
 */
public class QuicksortManager {
    private static LRUBufferPool bufferPoolInstance;
    private static final int SIZE_OF_RECORD = 4;
    private static final int INSERTION_SORT_THRESHOLD_MAX = 10;
    private static final int INSERTION_SORT_THRESHOLD_MIN = 3;

    /**
     * Constructs a new QuicksortManager instance and initiates the hybrid
     * sorting process on the dataset.
     *
     * @param pool
     *            The LRUBufferPool instance used for managing disk and memory
     *            data access.
     * @param lengthOfFile
     *            The length of the file to be sorted, in bytes.
     * @throws IOException
     *             If an I/O error occurs during the sorting process.
     */
    public QuicksortManager(LRUBufferPool pool, int lengthOfFile)
        throws IOException {
        bufferPoolInstance = pool;
        performQuickSortHybrid(0, (lengthOfFile / SIZE_OF_RECORD) - 1);

        insertionSort(0, (lengthOfFile / SIZE_OF_RECORD) - 1);
    }


    /**
     * Chooses a pivot index for the Quicksort partitioning process using the
     * median-of-three method.
     *
     * @param leftIndex
     *            The starting index of the segment to be sorted.
     * @param rightIndex
     *            The ending index of the segment to be sorted.
     * @return The index of the chosen pivot element.
     * @throws IOException
     *             If an I/O error occurs when fetching keys from the buffer
     *             pool.
     */
    private int choosePivotIndex(int leftIndex, int rightIndex)
        throws IOException {
        short firstKey = bufferPoolInstance.fetchKey(leftIndex);
        short middleKey = bufferPoolInstance.fetchKey((leftIndex + rightIndex)
            / 2);
        short lastKey = bufferPoolInstance.fetchKey(rightIndex);

        if ((firstKey > middleKey) ^ (firstKey > lastKey))
            return leftIndex;
        else if ((middleKey > firstKey) ^ (middleKey > lastKey))
            return (leftIndex + rightIndex) / 2;
        else
            return rightIndex;
    }


    /**
     * Performs the hybrid Quicksort on the specified segment of the dataset.
     * If the segment size falls within a specific range, the method returns
     * early,
     * deferring the final sorting to Insertion Sort.
     *
     * @param leftIndex
     *            The starting index of the segment to be sorted.
     * @param rightIndex
     *            The ending index of the segment to be sorted.
     * @throws IOException
     *             If an I/O error occurs during sorting.
     */
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


    /**
     * Partitions the dataset around a pivot element for the Quicksort
     * algorithm.
     *
     * @param leftIndex
     *            The starting index of the partition.
     * @param rightIndex
     *            The ending index of the partition.
     * @param pivot
     *            The pivot value used for partitioning.
     * @return The index where the pivot element is placed after partitioning.
     * @throws IOException
     *             If an I/O error occurs during partitioning.
     */
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


    /**
     * This method is intended for final tuning on smaller segments that are
     * nearly sorted.
     * It's more efficient on such segments due to its adaptive nature and low
     * overhead.
     *
     * @param leftIndex
     *            The starting index of the segment to be sorted.
     * @param rightIndex
     *            The ending index of the segment to be sorted.
     * @throws IOException
     *             If an I/O error occurs when accessing the buffer pool.
     */
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


    /**
     * Swaps two elements in the dataset by exchanging their positions.
     * This method is used by both the Quicksort and Insertion Sort algorithms
     * to reorder elements during the sorting process.
     *
     * @param firstPosition
     *            The index of the first element to be swapped.
     * @param secondPosition
     *            The index of the second element to be swapped.
     * @throws IOException
     *             If an I/O error occurs when accessing the buffer pool.
     */
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
