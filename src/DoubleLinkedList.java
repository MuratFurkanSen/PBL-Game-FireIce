public class DoubleLinkedList {
    private DLLNode head;
    private DLLNode tail;
    boolean isSorted = false;

    public DoubleLinkedList() {
        head = null;
        tail = null;
    }

    public DoubleLinkedList(boolean isSorted) {
        head = null;
        tail = null;
        this.isSorted = isSorted;
    }

    public void add(Object data, int priority) {
        if (isSorted) {
            sortedAdd(data, priority);
        }
    }
    public void add(Object data) {
        if (!isSorted) {
            simpleAdd(data);
        }
    }
    private void sortedAdd(Object data, int priority) {
        DLLNode newNode = new DLLNode(data, priority);
        if (head == null) {
            head = newNode;
            tail = newNode;
            return;
        }
        if (head.getPriority() < priority) {
            newNode.setNext(head);
            head.setPrev(newNode);
            head = newNode;
            return;
        }
        if (priority <= tail.getPriority()) {
            tail.setNext(newNode);
            newNode.setPrev(tail);
            tail = newNode;
            return;
        }
        DLLNode temp = head;
        DLLNode prev = null;
        while (temp != null && !(temp.getPriority() < priority)) {
            prev = temp;
            temp = temp.getNext();
        }
        prev.setNext(newNode);
        newNode.setPrev(prev);

        newNode.setNext(temp);
        temp.setPrev(newNode);
    }
    private void simpleAdd(Object data) {
        DLLNode newNode = new DLLNode(data);
        if (head == null) {
            head = newNode;
            tail = newNode;
        }
        tail.setNext(newNode);
        tail = newNode;
    }

    Object pop(){
        return pop(0);
    }
    Object pop(int index){
        if (head == null){
            System.out.println("List is empty");
            return null;
        }
        Object val;
        if (index == 0) {
            val = head.getData();
            if (head.getNext() != null){
                head.getNext().setPrev(null);
            }
            head = head.getNext();
            return val;
        }
        DLLNode temp = head;
        DLLNode prev = null;
        for (int i = 0; i < index; i++) {
            prev=temp;
            temp = temp.getNext();
        }
        val = temp.getData();
        if (temp == tail){
            prev.setNext(null);
            tail = prev;
        }
        else {
            temp.getNext().setPrev(prev);
            prev.setNext(temp.getNext());
        }
        return val;
    }

    void display(String sep){
        if (head == null) {
            return;
        }
        DLLNode temp = head;
        while (temp != null) {
            System.out.print(temp.getData() + sep);
            temp = temp.getNext();
        }
    }
    int size(){
        if (head == null) {
            return 0;
        }
        DLLNode temp = head;
        int size = 0;
        while (temp != null) {
            temp = temp.getNext();
            size++;
        }
        return size;
    }
    int max(){
        return tail.getPriority();
    }
    Object atIndex(int index){
        if (head == null){
            System.out.println("List is empty");
        }
        DLLNode temp = head;
        for (int i = 0; i < index; i++) {
            temp = temp.getNext();
        }
        return temp.getData();
    }
}
