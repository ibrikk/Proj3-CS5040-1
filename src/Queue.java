/**
 * Represents a queue specifically designed to manage buffer nodes in a Least
 * Recently Used (LRU) caching system.
 * This queue supports basic enqueue and dequeue operations, and it allows
 * searching for buffer nodes based on their position.
 * The queue is implemented as a doubly linked list to efficiently add and
 * remove elements at both ends.
 */
/**
 * @author {Ibrahim Khalilov} {Francisca Wood}
 * @version {ibrahimk} {fransciscawood}
 */
public class Queue {
    private Node<Buffer> tail;
    private Node<Buffer> head;
    private Node<Buffer> current;
    private int size;
    private int capacity;

    /**
     * Constructs a new Queue with a specified capacity.
     *
     * @param capacity
     *            The maximum number of elements the queue can hold.
     */
    public Queue(int capacity) {
        tail = new Node<Buffer>(null, null, null);
        current = tail;
        head = new Node<Buffer>(null, null, null);
        head.setNext(tail);
        tail.setPrev(head);
        this.capacity = capacity;
        setSize(0);
    }


    /**
     * Adds a new buffer to the queue. The new buffer is added right after the
     * head,
     * making it the most recently used (MRU) buffer in the queue.
     *
     * @param buff
     *            The buffer to be added to the queue.
     */
    public void enqueue(Buffer buff) {
        current = new Node<Buffer>(buff, head, head.getNext());
        head.setNext(current);
        current.getNext().setPrev(current);
        size++;
    }


    /**
     * Removes and returns the least recently used (LRU) buffer from the queue.
     * If the queue is empty, returns null.
     *
     * @return The buffer removed from the queue, or null if the queue is empty.
     */
    public Buffer dequeue() {
        if (size == 0) {
            return null;
        }
        current = tail.getPrev();
        Buffer val = current.getVal();
        current.getPrev().setNext(tail);
        tail.setPrev(current.getPrev());
        current = head.getNext();
        size--;
        return val;
    }


    /**
     * Searches for a buffer in the queue based on its position.
     *
     * @param pos
     *            The position of the buffer to search for.
     * @return The found buffer, or null if no buffer with the specified
     *         position exists in the queue.
     */
    public Buffer search(int pos) {
        Buffer found = null;
        Node<Buffer> temp = head.getNext();
        while (temp.getVal() != null) {
            if (temp.getVal().getPosition() == pos) {
                found = temp.getVal();
                break;
            }
            temp = temp.getNext();
        }
        return found;
    }


    /**
     * Gets the current size of the queue.
     *
     * @return The number of elements in the queue.
     */
    public int getSize() {
        return size;
    }


    /**
     * Sets the size of the queue. This method is primarily for internal use and
     * maintaining the integrity of the queue size.
     *
     * @param size
     *            The new size of the queue.
     */
    public void setSize(int size) {
        this.size = size;
    }


    /**
     * Gets the capacity of the queue.
     *
     * @return The maximum number of elements the queue can hold.
     */
    public int getCapacity() {
        return capacity;
    }


    /**
     * Sets the capacity of the queue. This method allows for dynamic adjustment
     * of the queue's capacity.
     *
     * @param capacity
     *            The new capacity of the queue.
     */
    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }


    /**
     * Marks buffer as recently used
     *
     * @param buffer
     *            The buffer that is recently used.
     */
    public void markAsRecentlyUsed(Buffer buffer) {
        Node<Buffer> node = findNodeForBuffer(buffer);
        if (node == null) {
            return;
        }

        node.getPrev().setNext(node.getNext());
        node.getNext().setPrev(node.getPrev());

        node.setNext(tail);
        node.setPrev(tail.getPrev());
        tail.getPrev().setNext(node);
        tail.setPrev(node);
    }


    /**
     * Marks buffer as recently used
     *
     * @param buffer
     *            Find the buffer in the queue.
     */
    private Node<Buffer> findNodeForBuffer(Buffer buffer) {
        Node<Buffer> current = head.getNext();
        while (current != tail) {
            if (current.getVal().equals(buffer)) {
                return current;
            }
            current = current.getNext();
        }
        return null;
    }
}
