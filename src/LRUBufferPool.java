import java.io.IOException;
import java.io.RandomAccessFile;

public class LRUBufferPool {
    private RandomAccessFile disk;
    private Queue lru;
    private boolean cacheFlag;
    private static final int BLOCK_SIZE = 4096;
    private static final int RECORD_SIZE = 4;
    private long executionTime;
    int hits = 0;
    int reads = 0;
    int writes = 0;

    public LRUBufferPool(RandomAccessFile file, int bufferCount)
        throws IOException {
        long startTime = System.currentTimeMillis();
        disk = file;
        int diskLength = (int)disk.length();
        lru = new Queue(bufferCount);
        for (int i = 0; i < bufferCount; i++) {
            lru.add(new Buffer(null, -1));
        }
        new QuicksortManager(this, diskLength);
        flush();
        executionTime = System.currentTimeMillis() - startTime;
    }


    public void storeBytes(
        byte[] fromArray,
        int bytesCopied,
        int destinationPos)
        throws IOException {
        Buffer found = getBuffer(destinationPos);
        int bufferPos = (destinationPos * RECORD_SIZE) % BLOCK_SIZE;
        byte[] temp = found.getArray();
        for (int i = 0; i < bytesCopied; i++) {
            temp[bufferPos++] = fromArray[i];
        }
        found.setArray(temp);
        found.setDirty(true);
    }


    public void retrieveBytes(
        byte[] fromArray,
        int bytesCopied,
        int destinationPos)
        throws IOException {
        Buffer found = getBuffer(destinationPos);
        int bufferPos = (destinationPos * RECORD_SIZE) % BLOCK_SIZE;
        byte[] temp = found.getArray();
        for (int i = 0; i < bytesCopied; i++) {
            fromArray[i] = temp[bufferPos++];
        }
    }


    public void removeFromPool() throws IOException {
        Buffer toBeRemoved = lru.remove();
        if (toBeRemoved != null && toBeRemoved.isDirty()) {
            disk.seek(toBeRemoved.getPosition() * BLOCK_SIZE);
            byte[] temp = toBeRemoved.getArray();
            disk.write(temp);
            writes += 1;
            disk.seek(0);
        }
    }


    public Buffer getBuffer(int pos) throws IOException {
        int bufferP = (pos * RECORD_SIZE) / BLOCK_SIZE;
        Buffer found = lru.find(bufferP);
        // if not in the buffer pool, add it to buffer pool from reading file
        if (found == null) {
            byte[] newBuff = new byte[BLOCK_SIZE];
            disk.seek(BLOCK_SIZE * bufferP);
            disk.read(newBuff, 0, BLOCK_SIZE);
            disk.seek(0);
            found = new Buffer(newBuff, bufferP);
            lru.add(found);
            if (lru.getSize() > lru.getCapacity()) {
                removeFromPool();
            }
            reads += 1;
            cacheFlag = false;
            return found;
        }
        // track as cache hit
        cacheFlag = true;
        return found;
    }


    public short retrieveKey(int index) throws IOException {
        short found = 0;
        if (cacheFlag) {
            hits++;
        }
        Buffer buf = getBuffer(index);
        int bufferPos = (index * RECORD_SIZE) % BLOCK_SIZE;
        found = buf.getKey(bufferPos);
        return found;
    }


    public void flush() throws IOException {
        while (lru.getSize() > 0) {
            removeFromPool();
        }
    }


    public int getHits() {
        return hits;
    }


    public int getReads() {
        return reads;
    }


    public int getWrites() {
        return writes;
    }


    public Queue getQueue() {
        return lru;
    }


    public long getTime() {
        return (executionTime / 1000000);
    }


    public void terminateFileOperation() throws IOException {
        disk.close();
    }

}
