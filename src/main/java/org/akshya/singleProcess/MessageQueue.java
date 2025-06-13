package org.akshya.singleProcess;

import java.util.LinkedList;
import java.util.Queue;

/**
 * MessageChannel wraps a synchronized queue using wait/notifyAll.
 */
public class MessageQueue {
    private final Queue<String> messages = new LinkedList<>();

    public synchronized void put(String msg) throws InterruptedException {
        while (!messages.isEmpty()) {
            wait();
        }

        messages.add(msg);
        notifyAll();
    }

    public synchronized String take() throws InterruptedException {
        while (messages.isEmpty()) {
            wait();
        }

        String msg = messages.poll();
        notifyAll();
        return msg;
    }
}
