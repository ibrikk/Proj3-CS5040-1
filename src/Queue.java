
public class Queue {
    private Node<Buffer> tail;
    private Node<Buffer> head;
    private Node<Buffer> current;
    private int size;
    private int capacity;

    public Queue(int capacity) {
        tail = new Node<Buffer>(null, null, null);
        current = tail;
        head = new Node<Buffer>(null, null, null);
        head.setNext(tail);
        tail.setPrev(head);
        this.capacity = capacity;
        setSize(0);
    }


    public void add(Buffer buff) {
        current = new Node<Buffer>(buff, head, head.getNext());
        head.setNext(current);
        current.getNext().setPrev(current);
        size++;
    }


    public Buffer remove() {
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


    public Buffer find(int pos) {
        Buffer ret = null;
        Node<Buffer> temp = head.getNext();
        while (temp.getVal() != null) {
            if (temp.getVal().getPosition() == pos) {
                ret = temp.getVal();
                break;
            }
            temp = temp.getNext();
        }
        return ret;
    }


    public int getSize() {
        return size;
    }


    public void setSize(int size) {
        this.size = size;
    }


    public int getCapacity() {
        return capacity;
    }


    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }
}




class Node<T> {
    private T val;
    private Node<T> next;
    private Node<T> prev;

    public Node(T val, Node<T> prev, Node<T> next) {
        this.next = next;
        this.prev = prev;
        this.val = val;
    }


    public T getVal() {
        return val;
    }


    public void setVal(T val) {
        this.val = val;
    }


    public Node<T> getNext() {
        return next;
    }


    public void setNext(Node<T> next) {
        this.next = next;
    }


    public Node<T> getPrev() {
        return prev;
    }


    public void setPrev(Node<T> prev) {
        this.prev = prev;
    }
}
