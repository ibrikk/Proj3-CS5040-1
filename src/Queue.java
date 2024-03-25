
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


    public void enqueue(Buffer buff) {
        current = new Node<Buffer>(buff, head, head.getNext());
        head.setNext(current);
        current.getNext().setPrev(current);
        size++;
    }


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
