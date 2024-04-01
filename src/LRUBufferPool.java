import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Manages a buffer pool using the Least Recently Used (LRU) strategy for
 * accessing blocks of data from disk.
 * This class is designed to minimize disk I/O by caching recently accessed data
 * blocks in memory.
 */
/**
 * @author {Ibrahim Khalilov} {Francisca Wood}
 * @version {ibrahimk} {fransciscawood}
 */
public class LRUBufferPool {
    private RandomAccessFile disk;
    private Queue cacheQueue;
    private boolean hitFlag;
    private static final int BLOCK_SIZE = 4096;
    private static final int RECORD_SIZE = 4;

    /**
     * Constructs a new LRUBufferPool for the specified disk file and buffer
     * count.
     *
     * @param file
     *            The disk file to be managed by the buffer pool.
     * @param bufferCount
     *            The number of buffers to allocate in the pool.
     * @throws IOException
     *             If an I/O error occurs reading from the disk file.
     */
    public LRUBufferPool(RandomAccessFile file, int bufferCount)
        throws IOException {
        disk = file;
        int diskLength = (int)disk.length();
        cacheQueue = new Queue(bufferCount);
        for (int i = 0; i < bufferCount; i++) {
            cacheQueue.enqueue(new Buffer(null, -1));
        }
        new QuicksortManager(this, diskLength);
        flush();
    }


    /**
     * Stores bytes from a given array into a specific position in the buffer
     * pool, potentially marking the buffer as dirty.
     *
     * @param fromArray
     *            The source byte array from which bytes are copied.
     * @param bytesCopied
     *            The number of bytes to copy.
     * @param destinationPos
     *            The position in the buffer pool where the bytes should be
     *            stored.
     * @throws IOException
     *             If an I/O error occurs during the operation.
     */
    public void storeBytes(
        byte[] fromArray,
        int bytesCopied,
        int destinationPos)
        throws IOException {
        Buffer found = locateBuffer(destinationPos);
        int bufferPos = (destinationPos * RECORD_SIZE) % BLOCK_SIZE;
        byte[] temp = found.getByteArray();
        for (int i = 0; i < bytesCopied; i++) {
            temp[bufferPos++] = fromArray[i];
        }
        found.setByteArray(temp);
        found.setDirty(true);
    }


    /**
     * Retrieves bytes from the buffer pool and stores them into a given array.
     *
     * @param fromArray
     *            The destination byte array where bytes will be copied.
     * @param bytesCopied
     *            The number of bytes to copy.
     * @param destinationPos
     *            The position in the buffer pool from which the bytes should be
     *            retrieved.
     * @throws IOException
     *             If an I/O error occurs during the operation.
     */
    public void retrieveBytes(
        byte[] fromArray,
        int bytesCopied,
        int destinationPos)
        throws IOException {
        Buffer found = locateBuffer(destinationPos);
        int bufferPos = (destinationPos * RECORD_SIZE) % BLOCK_SIZE;
        byte[] temp = found.getByteArray();
        for (int i = 0; i < bytesCopied; i++) {
            fromArray[i] = temp[bufferPos++];
        }
    }


    /**
     * Removes the least recently used (LRU) buffer from the pool and writes it
     * back to disk if it is dirty.
     *
     * @throws IOException
     *             If an I/O error occurs during the write-back operation.
     */
    public void removeFromPool() throws IOException {
        Buffer toBeRemoved = cacheQueue.dequeue();
        if (toBeRemoved != null && toBeRemoved.isDirty()) {
            disk.seek(toBeRemoved.getPosition() * BLOCK_SIZE);
            byte[] temp = toBeRemoved.getByteArray();
            disk.write(temp);
            Statistics.incrementWrites();
            disk.seek(0);
        }
    }


    /**
     * Locates a buffer in the pool corresponding to a specific position or
     * loads it from disk if not present.
     *
     * @param pos
     *            The position for which the buffer is sought.
     * @return The found or loaded buffer.
     * @throws IOException
     *             If an I/O error occurs during the operation.
     */
    private Buffer locateBuffer(int pos) throws IOException {
        int bufferIndex = (pos * RECORD_SIZE) / BLOCK_SIZE;
        Buffer found = cacheQueue.search(bufferIndex);
        if (found == null) {
            byte[] newBuff = new byte[BLOCK_SIZE];
            disk.seek(BLOCK_SIZE * bufferIndex);
            disk.read(newBuff, 0, BLOCK_SIZE);
            disk.seek(0);
            found = new Buffer(newBuff, bufferIndex);
            cacheQueue.enqueue(found);
            if (cacheQueue.getSize() > cacheQueue.getCapacity()) {
                removeFromPool();
            }
            Statistics.incrementReads();
            hitFlag = false;
            return found;
        }
        hitFlag = true;
        return found;
    }


    /**
     * Fetches the key (short value) stored at a specific index within a buffer.
     *
     * @param index
     *            The index within the buffer from which the key is to be
     *            fetched.
     * @return The fetched key.
     * @throws IOException
     *             If an I/O error occurs during the operation.
     */
    public short fetchKey(int index) throws IOException {
        short found = 0;
        Buffer buf = locateBuffer(index);
        if (hitFlag) {
            Statistics.incrementHits();
        }
        int bufferPos = (index * RECORD_SIZE) % BLOCK_SIZE;
        found = buf.extractKey(bufferPos);
        return found;
    }


    /**
     * Writes all dirty buffers back to disk and clears the buffer pool.
     *
     * @throws IOException
     *             If an I/O error occurs during the flush operation.
     */
    public void flush() throws IOException {
        while (cacheQueue.getSize() > 0) {
            removeFromPool();
        }
    }


    /**
     * Retrieves the queue managing the LRU strategy in the buffer pool.
     *
     * @return The LRU queue.
     */
    public Queue getQueue() {
        return cacheQueue;
    }


    /**
     * Closes the disk file stream associated with this buffer pool.
     *
     * @throws IOException
     *             If an I/O error occurs during the close operation.
     */
    public void closeFileStream() throws IOException {
        disk.close();
    }

}
