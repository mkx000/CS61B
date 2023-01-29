public class ArrayDeque<T> {

    private class Array {
        private T[] arr;

        public Array(int size) {
            arr = (T[]) new Object[size];
        }
    }

    private int actualSize;
    private int logicalSize;
    private int head;
    private int tail;

    private static int initialSize = 4;

    private Array array;

    public ArrayDeque() {
        this.logicalSize = 0;
        this.actualSize = initialSize;
        this.array = new Array(this.actualSize);

        this.head = 0;
        this.tail = 1;
    }

    public void addFirst(T item) {
        if (2 * this.logicalSize >= this.actualSize)
            this.bigger();

        this.array.arr[this.head] = item;
        int pos = (this.head - 1 + this.actualSize) % this.actualSize;
        this.head = pos;
        this.logicalSize++;
    }

    public void addLast(T item) {
        if (2 * this.logicalSize >= this.actualSize)
            this.bigger();

        this.array.arr[this.tail] = item;
        int pos = (this.tail + 1) % this.actualSize;
        this.tail = pos;
        this.logicalSize++;
    }

    public boolean isEmpty() {
        return this.logicalSize == 0;
    }

    public int size() {
        return this.logicalSize;
    }

    public void printDeque() {
        int pos = this.head + 1;
        for (int i = 0; i < this.logicalSize; i++) {
            if (i != 0)
                System.out.print(" ");
            System.out.print(this.array.arr[pos]);
            pos = (pos + 1) % this.logicalSize;
        }
    }

    public T removeFirst() {
        if (this.isEmpty())
            return null;
        if (this.actualSize > 2 * ArrayDeque.initialSize && 4 * this.logicalSize <= this.actualSize)
            this.smaller();
        this.logicalSize--;
        this.head = (this.head + 1) % this.actualSize;
        return this.array.arr[this.head];
    }

    public T removeLast() {
        if (this.isEmpty())
            return null;
        if (this.actualSize > 2 * ArrayDeque.initialSize && 4 * this.logicalSize <= this.actualSize)
            this.smaller();
        this.logicalSize--;
        this.tail = (this.tail - 1 + this.actualSize) % this.actualSize;
        return this.array.arr[this.tail];
    }

    public T get(int index) {
        if (index < 0 || index > this.logicalSize - 1)
            return null;
        int pos = (this.head + 1 + index) % this.actualSize;
        return this.array.arr[pos];
    }

    private void bigger() {
        int num = this.actualSize;
        int pos = (this.head + 1) % this.actualSize;
        Array old = this.array;
        this.array = new Array(2 * this.actualSize);

        for (int i = 0; i < num; i++) {
            this.array.arr[i + 1] = old.arr[pos];
            pos = (pos + 1) % num;
        }

        this.actualSize *= 2;
        this.head = 0;
        this.tail = num + 1;
    }

    private void smaller() {
        int num = this.actualSize;
        int pos = (this.head + 1) % this.actualSize;
        Array old = this.array;
        this.array = new Array(this.actualSize / 2);

        for (int i = 0; i < num; i++) {
            this.array.arr[i + 1] = old.arr[pos];
            pos = (pos + 1) % num;
        }
        this.actualSize /= 2;
        this.head = 0;
        this.tail = num + 1;
    }
}
