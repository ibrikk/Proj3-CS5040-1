/**
 * @author {Ibrahim Khalilov} {Francisca Wood}
 * @version {ibrahimk} {fransciscawood}
 */
/**
 * Represents a generic node in a doubly linked list, holding a value and
 * references to both the next and previous nodes in the list.
 * This class is designed to be used in data structures that require
 * bidirectional navigation, such as doubly linked lists.
 *
 * @param <T>
 *            The type of the value stored in the node.
 */
public class Node<T> {
    private T val;
    private Node<T> next;
    private Node<T> prev;

    /**
     * Constructs a new Node instance with specified value, previous node, and
     * next node.
     *
     * @param val
     *            The value to be stored in the node.
     * @param prev
     *            The previous node in the list. Can be null if this node is the
     *            first node.
     * @param next
     *            The next node in the list. Can be null if this node is the
     *            last node.
     */
    public Node(T val, Node<T> prev, Node<T> next) {
        this.next = next;
        this.prev = prev;
        this.val = val;
    }


    /**
     * Retrieves the value stored in this node.
     *
     * @return The value stored in the node.
     */
    public T getVal() {
        return val;
    }


    /**
     * Sets or updates the value stored in this node.
     *
     * @param val
     *            The new value to store in the node.
     */
    public void setVal(T val) {
        this.val = val;
    }


    /**
     * Retrieves the next node in the list.
     *
     * @return The next node. Returns null if this node is the last node in the
     *         list.
     */
    public Node<T> getNext() {
        return next;
    }


    /**
     * Sets or updates the reference to the next node in the list.
     *
     * @param next
     *            The new next node. Can be null if this node is to become the
     *            last node in the list.
     */
    public void setNext(Node<T> next) {
        this.next = next;
    }


    /**
     * Retrieves the previous node in the list.
     *
     * @return The previous node. Returns null if this node is the first node in
     *         the list.
     */
    public Node<T> getPrev() {
        return prev;
    }


    /**
     * Sets or updates the reference to the previous node in the list.
     *
     * @param prev
     *            The new previous node. Can be null if this node is to become
     *            the first node in the list.
     */
    public void setPrev(Node<T> prev) {
        this.prev = prev;
    }
}
