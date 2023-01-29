public class LinkedListDeque<T> {

    private class Node {
        private T item;
        private Node prev;
        private Node next;

        public Node(T item) {
            this.item = item;
            this.prev = null;
            this.next = null;
        }

        public Node() {
            this.prev = null;
            this.next = null;
        }
    }


    private Node sentinel;
    private int dqsize;

    public LinkedListDeque() {
        Node dummyHead = new Node();
        dummyHead.prev = dummyHead;
        dummyHead.next = dummyHead;
        this.sentinel = dummyHead;
        this.dqsize = 0;
    }

    public void addFirst(T item) {
        Node node = new Node(item);

        this.sentinel.next.prev = node;
        node.next = this.sentinel.next;

        this.sentinel.next = node;
        node.prev = this.sentinel;

        this.dqsize++;
    }

    public void addLast(T item) {
        Node node = new Node(item);

        node.prev = this.sentinel.prev;
        this.sentinel.prev.next = node;

        node.next = this.sentinel;
        this.sentinel.prev = node;

        this.dqsize++;
    }

    public boolean isEmpty() {
        return this.dqsize == 0;
    }

    public int size() {
        return this.dqsize;
    }

    public void printDeque() {
        Node node = this.sentinel.next;

        boolean first = true;
        while (node != this.sentinel) {
            if (first)
                first = false;
            else
                System.out.print(" ");
            System.out.print(node.item);
            node = node.next;
        }
    }

    public T removeFirst() {
        if (isEmpty())
            return null;
        T item = this.sentinel.next.item;
        this.sentinel.next.next.prev = this.sentinel;
        this.sentinel.next = this.sentinel.next.next;
        this.dqsize--;
        return item;
    }

    public T removeLast() {
        if (isEmpty())
            return null;
        T item = this.sentinel.prev.item;
        this.sentinel.prev.prev.next = this.sentinel;
        this.sentinel.prev = this.sentinel.prev.prev;
        this.dqsize--;
        return item;
    }

    public T get(int index) {
        if (index > dqsize - 1 || index < 0)
            return null;
        Node node = this.sentinel.next;
        while (index != 0) {
            node = node.next;
            index--;
        }
        return node.item;
    }


    public T getRecursive(int index) {
        if (index > dqsize - 1 || index < 0)
            return null;
        return getRecursive(index, this.sentinel.next);
    }

    private T getRecursive(int index, Node node) {
        if (index == 0)
            return node.item;
        return getRecursive(index - 1, node.next);
    }
}
