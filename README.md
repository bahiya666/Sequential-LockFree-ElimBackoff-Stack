Comparison of Different Stack Implementations: Sequential, Lock-Free, and Elimination Backoff Stacks
This project demonstrates and compares three different types of stack implementations with varying concurrency control mechanisms: Sequential Stack (Blocking), Lock-Free Stack, and Elimination Backoff Stack. These stacks are designed to handle concurrent push and pop operations efficiently while managing thread contention.

a. Sequential Stack (Blocking)
Description: A simple stack implementation using a LinkedList. This version synchronizes the push and pop methods using the synchronized keyword, making it thread-safe.
Use Case: Best for low contention environments where threads won't be frequently competing for the stack's resources.

b. Lock-Free Stack
Description: A stack implementation that uses an atomic reference to a linked list of nodes. The push and pop operations utilize CAS (Compare-And-Swap) to ensure thread safety without locking.
Use Case: Ideal for scenarios where contention is high, and blocking could result in significant performance degradation.

c. Elimination Backoff Stack
Description: This implementation adds an elimination backoff strategy where threads try to eliminate each other’s operations by waiting for a period before retrying. This reduces contention by decreasing the number of threads competing at any given time.
Use Case: Useful in highly contended scenarios to reduce contention and improve throughput.

Test Scenarios
In this project, we simulate both low contention and high contention environments to evaluate how each stack performs under different threading conditions.

Low Contention: Involves fewer threads (10-50) and focuses on the performance of stack operations when there are relatively few competitors for resources.

High Contention: Involves more threads (100-500) to simulate highly competitive environments where multiple threads are trying to access the stack concurrently.

 Code Structure
Stack Interface: Defines basic operations (push and pop) for the stack implementations.
SequentialStack: A simple, blocking implementation using LinkedList and synchronized methods.
LockFreeStack: A thread-safe stack using atomic operations (CAS).
EliminationBackoffStack: A stack implementing an elimination backoff strategy to reduce contention.
Main Class: Contains the performance tests and manages the execution of stack operations under different thread loads.

Metrics and Results
SequentialStack: Performs well under low contention but shows significant degradation as thread contention increases.
LockFreeStack: Scales better with higher thread contention, offering better performance than the sequential stack in high-contention environments.
EliminationBackoffStack: Outperforms both sequential and lock-free stacks in scenarios with high contention, as the backoff strategy reduces the number of competing threads, although it has its limits in extreme contention scenarios.

Conclusion
Best for Low Contention: SequentialStack is efficient when the number of threads is small and contention is low.
Best for High Contention: EliminationBackoffStack offers superior performance under high contention due to its backoff strategy.
Lock-Free Stack: Offers a middle ground, performing better than the sequential stack under high contention but not as well as the elimination backoff strategy.
