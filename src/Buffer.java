import java.nio.ByteBuffer;

public class Buffer {
    private byte[] bytes;
    private boolean dirty;
    private int pos;

    public Buffer(byte[] bytes, int pos) {
        if (bytes != null) {
            this.bytes = new byte[bytes.length];
            setByteArray(bytes);
        }
        dirty = false;
        setPosition(pos);
    }


    public byte[] getByteArray() {
        return bytes;
    }


    public void setByteArray(byte[] newData) {
        System.arraycopy(newData, 0, this.bytes, 0, newData.length);
    }


    public boolean isDirty() {
        return dirty;
    }


    public void setDirty(boolean dirty) {
        this.dirty = dirty;
    }


    public int getPosition() {
        return pos;
    }


    public void setPosition(int pos) {
        this.pos = pos;
    }


    public short extractKey(int id) {
        short key = 0;
        ByteBuffer bbuffer = ByteBuffer.allocate(2);
        bbuffer.put(bytes, id, 2);
        key = bbuffer.getShort(0);
        return key;
    }
}
