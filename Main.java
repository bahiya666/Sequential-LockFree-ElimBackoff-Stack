import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

public class Main {

    private static final int NUM_OPERATIONS = 10000; // Number of operations per thread

    public static void main(String[] args) throws InterruptedException {
        // Run tests for low contention
        System.out.println("=== Low Contention Testing ===");
        System.out.println("Testing Sequential/Blocking Stack:");
        testStackPerformance(new SequentialStack<>(), () -> 1, 10);
        testStackPerformance(new SequentialStack<>(), () -> 1, 20);
        testStackPerformance(new SequentialStack<>(), () -> 1, 30);
        testStackPerformance(new SequentialStack<>(), () -> 1, 40);
        testStackPerformance(new SequentialStack<>(), () -> 1, 50);

        System.out.println("Testing Lock-Free Stack:");
        testStackPerformance(new LockFreeStack<>(), () -> 1, 10);
        testStackPerformance(new LockFreeStack<>(), () -> 1, 20);
        testStackPerformance(new LockFreeStack<>(), () -> 1, 30);
        testStackPerformance(new LockFreeStack<>(), () -> 1, 40);
        testStackPerformance(new LockFreeStack<>(), () -> 1, 50);

        System.out.println("Testing Elimination Backoff Stack:");
        testStackPerformance(new EliminationBackoffStack<>(), () -> 1, 10);
        testStackPerformance(new EliminationBackoffStack<>(), () -> 1, 20);
        testStackPerformance(new EliminationBackoffStack<>(), () -> 1, 30);
        testStackPerformance(new EliminationBackoffStack<>(), () -> 1, 40);
        testStackPerformance(new EliminationBackoffStack<>(), () -> 1, 50);

        // Run tests for high contention
        System.out.println("\n=== High Contention Testing ===");
        System.out.println("Testing Sequential/Blocking Stack:");
        testStackPerformance(new SequentialStack<>(), () -> 1, 100);
        testStackPerformance(new SequentialStack<>(), () -> 1, 200);
        testStackPerformance(new SequentialStack<>(), () -> 1, 300);
        testStackPerformance(new SequentialStack<>(), () -> 1, 400);
        testStackPerformance(new SequentialStack<>(), () -> 1, 500);
        
        System.out.println("Testing Lock-Free Stack:");
        testStackPerformance(new LockFreeStack<>(), () -> 1, 100);
        testStackPerformance(new LockFreeStack<>(), () -> 1, 200);
        testStackPerformance(new LockFreeStack<>(), () -> 1, 300);
        testStackPerformance(new LockFreeStack<>(), () -> 1, 400);
        testStackPerformance(new LockFreeStack<>(), () -> 1, 500);

        System.out.println("Testing Elimination Backoff Stack:");
        testStackPerformance(new EliminationBackoffStack<>(), () -> 1, 100);
        testStackPerformance(new EliminationBackoffStack<>(), () -> 1, 200);
        testStackPerformance(new EliminationBackoffStack<>(), () -> 1, 300);
        testStackPerformance(new EliminationBackoffStack<>(), () -> 1, 400);
        testStackPerformance(new EliminationBackoffStack<>(), () -> 1, 500);
    }

    // Use a generic type T to ensure type safety
    private static <T> void testStackPerformance(Stack<T> stack, Supplier<T> supplier, int numThreads) throws InterruptedException {
        long startTime = System.nanoTime();

        // Create a thread pool for concurrent execution
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);

        for (int i = 0; i < numThreads; i++) {
            executor.submit(() -> {
                for (int j = 0; j < NUM_OPERATIONS; j++) {
                    stack.push(supplier.get()); // Use the supplier to get a new value
                    stack.pop();
                }
            });
        }

        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.MINUTES);

        long endTime = System.nanoTime();
        long duration = endTime - startTime; // Duration in nanoseconds

        // Print the number of threads and the time taken
        System.out.printf("Threads: %d, Time taken: %.3f seconds%n", numThreads, duration / 1_000_000_000.0);

        // If using EliminationBackoffStack, print metrics
        if (stack instanceof EliminationBackoffStack) {
            EliminationBackoffStack<T> eliminationStack = (EliminationBackoffStack<T>) stack;
            long successfulEliminations = eliminationStack.getSuccessfulEliminations();
            System.out.printf("Successful Eliminations: %d%n", 
                successfulEliminations);
        }
    }
    }

