import java.io.IOException;

public interface BufferPool {
    public void insert(byte[] fromArray, int bytesCopied, int destinationPos)
        throws IOException;


    public void getBytes(byte[] fromArray, int bytesCopied, int destinationPos)
        throws IOException;
}
