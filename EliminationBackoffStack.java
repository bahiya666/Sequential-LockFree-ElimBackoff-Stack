import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicLong;

public class EliminationBackoffStack<T> implements Stack<T> {
    private static class Node<T> {
        T value;
        Node<T> next;

        Node(T value) {
            this.value = value;
        }
    }

    private final AtomicReference<Node<T>> head = new AtomicReference<>();
    private final int BACKOFF_LIMIT = 100; // Limit for backoff strategy

    // Metrics
    private final AtomicLong successfulEliminations = new AtomicLong(0);
    private final AtomicLong totalWaitTime = new AtomicLong(0);

    @Override
    public void push(T value) {
        Node<T> newHead = new Node<>(value);
        while (true) {
            Node<T> currentHead = head.get();
            newHead.next = currentHead;

            // Attempt elimination before pushing
            long waitStart = System.nanoTime(); // Start timing before attempting elimination
            if (tryEliminate(value)) {
                long waitEnd = System.nanoTime(); // End timing after successful elimination
                return; // Successful elimination
            }
            long waitEnd = System.nanoTime(); // End timing after attempted elimination

            // Attempt to push onto the stack
            if (head.compareAndSet(currentHead, newHead)) {
                return; // Successful push
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

            // Attempt to pop from the stack
            if (head.compareAndSet(currentHead, currentHead.next)) {
                return currentHead.value;
            }
        }
    }

    private boolean tryEliminate(T value) {
        // Simulated elimination logic (for demonstration)
        // In practice, this should be a more sophisticated approach
        if (ThreadLocalRandom.current().nextInt(0, 100) < 10) { // 10% chance to eliminate
            successfulEliminations.incrementAndGet(); // Increment on successful elimination
            return true; // Assume elimination was successful
        }
        return false;
    }


    public long getSuccessfulEliminations() {
        return successfulEliminations.get(); // Get the count of successful eliminations
    }

   
}
