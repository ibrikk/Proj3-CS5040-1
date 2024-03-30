import java.nio.ByteBuffer;

/**
 * Represents a buffer that stores a segment of data, typically used for
 * buffering data blocks
 * read from or written to a disk in a larger file management or database
 * system. It supports
 * operations for reading and writing data, marking the buffer as dirty
 * (modified), and extracting
 * specific pieces of data such as keys from the buffered data.
 */
public class Buffer {
    private byte[] bytes;
    private boolean dirty;
    private int pos;

    /**
     * Constructs a new Buffer instance with specified data and position.
     * The buffer is initially marked as not dirty.
     *
     * @param bytes
     *            The byte array of data to store in the buffer. If null, the
     *            buffer is initialized empty.
     * @param pos
     *            The position or identifier of the buffer.
     */
    public Buffer(byte[] bytes, int pos) {
        if (bytes != null) {
            this.bytes = new byte[bytes.length];
            setByteArray(bytes);
        }
        dirty = false;
        setPosition(pos);
    }


    /**
     * Retrieves the byte array stored in this buffer.
     *
     * @return The byte array representing the data in the buffer.
     */
    public byte[] getByteArray() {
        return bytes;
    }


    /**
     * Sets or updates the byte array stored in this buffer with new data.
     *
     * @param newData
     *            The new byte array data to store in the buffer.
     */
    public void setByteArray(byte[] newData) {
        System.arraycopy(newData, 0, this.bytes, 0, newData.length);
    }


    /**
     * Checks whether this buffer has been marked as dirty (modified).
     *
     * @return true if the buffer is dirty, false otherwise.
     */
    public boolean isDirty() {
        return dirty;
    }


    /**
     * Marks this buffer as dirty (modified) or not dirty based on the given
     * flag.
     *
     * @param dirty
     *            true to mark the buffer as dirty, false to mark it as not
     *            dirty.
     */
    public void setDirty(boolean dirty) {
        this.dirty = dirty;
    }


    /**
     * Retrieves the position or identifier of this buffer.
     *
     * @return The position or identifier of the buffer.
     */
    public int getPosition() {
        return pos;
    }


    /**
     * Sets the position or identifier of this buffer.
     *
     * @param pos
     *            The new position or identifier for the buffer.
     */
    public void setPosition(int position) {
        this.pos = position;
    }


    /**
     * Extracts a short value (key) from the buffer at the specified index.
     * This method is typically used to retrieve specific pieces of data, such
     * as keys, from the buffer.
     *
     * @param id
     *            The index within the buffer from which to extract the key.
     * @return The extracted short value (key) from the buffer.
     */
    public short extractKey(int id) {
        short key = 0;
        ByteBuffer bbuffer = ByteBuffer.allocate(2);
        bbuffer.put(bytes, id, 2);
        key = bbuffer.getShort(0);
        return key;
    }
}
