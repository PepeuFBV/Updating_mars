package mars.mips.SO.ProcessManager;

import java.util.concurrent.Semaphore;
public class Buffer {

    /*private static class Semaphore {
        int permits;

        public Semaphore(int permits) {
            this.permits = permits;
        }

        public void acquire() {}

        public void release() {}
    }*/

    private final int[] items;
    private final int size;
    private final Semaphore mutex;
    private final Semaphore empty;
    private final Semaphore full;
    private int in;
    private int out;

    public Buffer(int size) {
        this.size = size;
        this.items = new int[size];
        this.in = 0;
        this.out = 0;
        this.mutex = new Semaphore(1);
        this.empty = new Semaphore(size);
        this.full = new Semaphore(0);
    }

    public void produce(int item) throws InterruptedException {
        empty.acquire(); // Wait if buffer is full
        mutex.acquire(); // Acquire mutex to modify buffer
        items[in] = item;
        in = (in + 1) % size;
        System.out.println("Produced: " + item);
        mutex.release(); // Release mutex
        full.release(); // Signal that buffer is not empty
    }

    public int consume() throws InterruptedException {
        full.acquire(); // Wait if buffer is empty
        mutex.acquire(); // Acquire mutex to modify buffer
        int item = items[out];
        out = (out + 1) % size;
        System.out.println("Consumed: " + item);
        mutex.release(); // Release mutex
        empty.release(); // Signal that buffer is not full
        return item;
    }
}
