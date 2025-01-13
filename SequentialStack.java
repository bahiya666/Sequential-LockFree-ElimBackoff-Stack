import java.util.LinkedList;

public class SequentialStack<T> implements Stack<T> {
    private LinkedList<T> stack = new LinkedList<>();

    @Override
    public synchronized void push(T value) {
        stack.addFirst(value);
    }

    @Override
    public synchronized T pop() {
        if (stack.isEmpty()) {
            return null; // or throw an exception
        }
        return stack.removeFirst();
    }
}
