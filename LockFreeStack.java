import java.util.concurrent.atomic.AtomicReference;

public class LockFreeStack<T> implements Stack<T> {
    private static class Node<T> {
        T value;
        Node<T> next;

        Node(T value) {
            this.value = value;
        }
    }

    private final AtomicReference<Node<T>> head = new AtomicReference<>();

    @Override
    public void push(T value) {
        Node<T> newHead = new Node<>(value);
        while (true) {
            Node<T> currentHead = head.get();
            newHead.next = currentHead;
            if (head.compareAndSet(currentHead, newHead)) {
                return;
            }
        }
    }

    @Override
    public T pop() {
        while (true) {
            Node<T> currentHead = head.get();
            if (currentHead == null) {
                return null; // or throw an exception
            }
            if (head.compareAndSet(currentHead, currentHead.next)) {
                return currentHead.value;
            }
        }
    }
}
