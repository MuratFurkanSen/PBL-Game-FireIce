import java.util.Random;

public class LinkedList {
    private Node head;
    private boolean isSorted = false;
    LinkedList(){
        head = null;
    }
    LinkedList(boolean isSorted){
        head = null;
        this.isSorted = isSorted;

    }

    void add(Object data){
        if (isSorted){
            sortedAdd(data);
        }
        else {
            simpleAdd(data);
        }
    }
    private void simpleAdd(Object data){
        Node newNode = new Node(data);
        if (head == null) {
            head = newNode;
            return;
        }
        Node temp = head;
        while (temp.getLink() != null){
            temp = temp.getLink();
        }
        temp.setLink(newNode);
    }
    private void sortedAdd(Object data){
        Node newNode = new Node(data);
        if (head == null) {
            head = newNode;
            return;
        }
        if ((int) head.getData() > (int) data) {
            newNode.setLink(head);
            head = newNode;
            return;
        }
        if (size() == 1){
            head.setLink(newNode);
            return;
        }
        Node temp = head;
        Node prev = null;
        while (temp != null && !((int) temp.getData() > (int) data)){
            prev = temp;
            temp = temp.getLink();
        }
        prev.setLink(newNode);
        newNode.setLink(temp);
    }

    Object pop(int index){
        if (head == null){
            System.out.println("List is empty");
        }
        Object val;
        if (index == 0) {
            val = head.getData();
            head = head.getLink();
            return val;
        }
        Node temp = head;
        Node prev = null;
        for (int i = 0; i < index; i++) {
            prev=temp;
            temp = temp.getLink();
        }
        val = temp.getData();
        prev.setLink(temp.getLink());
        return val;
    }
    // For a Default Pop Method
    Object pop() {
        return pop(0);
    }
    void remove(Object data){
        if (head == null) {
            System.out.println("List is empty");
            return;
        }
        while (head != null && head.getData().equals(data)) {
            head = head.getLink();
        }
        Node temp = head;
        Node prev = null;
        while (temp != null ) {
            if (temp.getData().equals(data)) {
                prev.setLink(temp.getLink());
                temp = prev;
            }
            prev= temp;
            temp = temp.getLink();
        }
    }

    void display(){
        if (head == null) {
            return;
        }
        Node temp = head;
        while (temp != null) {
            System.out.print(temp.getData()+" ");
            temp = temp.getLink();
        }
    }
    Object atIndex(int index){
        if (head == null){
            System.out.println("List is empty");
        }
        Node temp = head;
        for (int i = 0; i < index; i++) {
            temp = temp.getLink();
        }
        return temp.getData();
    }
    int size(){
        if (head == null) {
            return 0;
        }
        Node temp = head;
        int size = 0;
        while (temp != null) {
            temp = temp.getLink();
            size++;
        }
        return size;
    }
    int count(Object data){
        if (head == null){
            return 0;
        }
        Node temp = head;
        int count = 0;
        while (temp != null) {
            if (temp.getData().equals(data)) {
                count++;
            }
            temp = temp.getLink();
        }
        return count;
    }
    boolean contains(Object data){
        if (head == null){
            return false;
        }
        Node temp = head;
        while (temp != null) {
            if (temp.getData().equals(data)) {
                return true;
            }
            temp = temp.getLink();
        }
        return false;
    }

    Object getRandom(){
        if (head == null){
            return null;
        }
        Random rand = new Random();
        int selected = rand.nextInt(size());
        Node temp = head;
        for (int i = 0; i < selected; i++) {
            temp = temp.getLink();
        }
        return temp.getData();

    }
    Object popRandom(){
        if (head == null) {
            return null;
        }
        Random rand = new Random();
        int selected = rand.nextInt(size());
        Node temp = head;
        Node prev = null;
        for(int i = 0; i<selected; i++){
            prev = temp;
            temp = temp.getLink();
        }
        if (prev == null){
            head = head.getLink();
        }
        else {
            if (temp.getLink() == null){
                prev.setLink(null);
            }
            else {
                prev.setLink(temp.getLink());
            }
        }
        return temp.getData();

    }
    void shuffle(){
        LinkedList shuffled = new LinkedList();
        while (size() != 0){
            shuffled.simpleAdd(popRandom());
        }
        head = shuffled.head;
    }

    LinkedList copy(){
        LinkedList copy = new LinkedList();
        Node temp = head;
        while (temp != null){
            copy.simpleAdd(temp.getData());
            temp = temp.getLink();
        }
        return copy;
    }
}
