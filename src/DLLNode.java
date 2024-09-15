public class DLLNode {
    private DLLNode prev;
    private DLLNode next;
    private Object data;
    private int priority;

    public DLLNode(Object data, int priority) {
        prev = null;
        next = null;
        this.data = data;
        this.priority = priority;
    }
    DLLNode(Object data){
        prev = null;
        next = null;
        this.data = data;
    }

    public DLLNode getPrev() {
        return prev;
    }

    public void setPrev(DLLNode prev) {
        this.prev = prev;
    }

    public DLLNode getNext() {
        return next;
    }

    public void setNext(DLLNode next) {
        this.next = next;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public int getPriority() {
        return priority;
    }
}
