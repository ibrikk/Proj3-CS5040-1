public class Node<T> {
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
