import java.util.EmptyStackException;

public class MyStack<E> {
    private class Node<E> {
        public E data;
        public Node<E> next;

        public Node(E data) {
            this.data = data;
        }
    }

    private Node<E> top;

    public void push(E data) {
        Node<E> temp = new Node<E>(data);
        if (top == null) {
            top = temp;
        } else {
            temp.next = top;
            top = temp;
        }
    }

    public E pop() throws EmptyStackException {
        if (top == null) {
            throw new EmptyStackException();
        } else {
            Node<E> temp = top;
            top = temp.next;
            return temp.data;
        }
    }

    public E peek() throws EmptyStackException {
        if (top == null) {
            throw new EmptyStackException();
        } else {
            return top.data;
        }
    }

    public boolean empty() {
        if (top == null) {
            return true;
        }
        return false;
    }
}
