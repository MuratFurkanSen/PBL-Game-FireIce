public class Stack {

    private Object[] elements;
    private int pointer;


    Stack(int capacity) {
        elements = new Object[capacity];
        pointer = -1;
    }

    void push(Object data) {
        if (!isFull()) {
            pointer++;
            elements[pointer] = data;
        }
    }
    Object pop() {
        if (isEmpty()) {
            return null;
        } else {
            Object temp = elements[pointer];
            elements[pointer] = null;
            pointer--;
            return temp;
        }
    }
    Object peek() {
        if (isEmpty()) {
            return null;
        } else {
            return elements[pointer];
        }
    }

    boolean isEmpty() {
        return pointer== -1;
    }
    boolean isFull() {
        return pointer+1 == elements.length ;
    }
    int size() {
        return pointer + 1;
    }
}
