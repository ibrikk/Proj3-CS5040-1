import java.nio.ByteBuffer;

public class Buffer {
    private byte[] bytes;
    private boolean dirty;
    private int pos;

    public Buffer(byte[] bytes, int pos) {
        if (bytes != null) {
            this.bytes = new byte[bytes.length];
            setArray(bytes);
        }
        dirty = false;
        setPosition(pos);
    }


    public byte[] getArray() {
        return bytes;
    }


    public void setArray(byte[] bytes) {
        for (int i = 0; i < bytes.length; i++) {
            this.bytes[i] = bytes[i];
        }
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


    public short retrieveKey(int id) {
        short key = 0;
        ByteBuffer bbuffer = ByteBuffer.allocate(2);
        bbuffer.put(bytes, id, 2);
        key = bbuffer.getShort(0);
        return key;
    }
}
