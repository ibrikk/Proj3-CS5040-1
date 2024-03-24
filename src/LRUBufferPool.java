import java.io.IOException;
import java.io.RandomAccessFile;

public class LRUBufferPool {
    private RandomAccessFile disk;
    private Queue cacheQueue;
    private boolean hitFlag;
    private static final int BLOCK_SIZE = 4096;
    private static final int RECORD_SIZE = 4;

    public LRUBufferPool(RandomAccessFile file, int bufferCount)
        throws IOException {
        long startTime = System.currentTimeMillis();
        disk = file;
        int diskLength = (int)disk.length();
        cacheQueue = new Queue(bufferCount);
        for (int i = 0; i < bufferCount; i++) {
            cacheQueue.enqueue(new Buffer(null, -1));
        }
        new QuicksortManager(this, diskLength);
        flush();
        Statistics.executionTime = System.currentTimeMillis() - startTime;
    }


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


    public void removeFromPool() throws IOException {
        Buffer toBeRemoved = cacheQueue.dequeue();
        if (toBeRemoved != null && toBeRemoved.isDirty()) {
            disk.seek(toBeRemoved.getPosition() * BLOCK_SIZE);
            byte[] temp = toBeRemoved.getByteArray();
            disk.write(temp);
            Statistics.writes++;
            disk.seek(0);
        }
    }


    public Buffer locateBuffer(int pos) throws IOException {
        int bufferIndex = (pos * RECORD_SIZE) / BLOCK_SIZE;
        Buffer found = cacheQueue.search(bufferIndex);
        if (found == null) {
            // If Buffer pool is null, then create
            byte[] newBuff = new byte[BLOCK_SIZE];
            disk.seek(BLOCK_SIZE * bufferIndex);
            disk.read(newBuff, 0, BLOCK_SIZE);
            disk.seek(0);
            found = new Buffer(newBuff, bufferIndex);
            cacheQueue.enqueue(found);
            if (cacheQueue.getSize() > cacheQueue.getCapacity()) {
                removeFromPool();
            }
            Statistics.reads++;
            hitFlag = false;
            return found;
        }
        hitFlag = true;
        return found;
    }


    public short fetchKey(int index) throws IOException {
        short found = 0;
        if (hitFlag) {
            Statistics.hits++;
        }
        Buffer buf = locateBuffer(index);
        int bufferPos = (index * RECORD_SIZE) % BLOCK_SIZE;
        found = buf.extractKey(bufferPos);
        return found;
    }


    public void flush() throws IOException {
        while (cacheQueue.getSize() > 0) {
            removeFromPool();
        }
    }


    public Queue getQueue() {
        return cacheQueue;
    }


    public void closeFileStream() throws IOException {
        disk.close();
    }

}
