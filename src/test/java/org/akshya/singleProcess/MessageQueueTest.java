package org.akshya.singleProcess;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class MessageQueueTest {

    @Test
    void testPutAndTake() throws InterruptedException {
        MessageQueue queue = new MessageQueue();

        Thread producer = new Thread(() -> {
            try {
                queue.put("test message");
            } catch (InterruptedException e) {
                fail("Producer was interrupted");
            }
        });

        Thread consumer = new Thread(() -> {
            try {
                String msg = queue.take();
                assertEquals("test message", msg);
            } catch (InterruptedException e) {
                fail("Consumer was interrupted");
            }
        });

        producer.start();
        Thread.sleep(100);
        consumer.start();

        producer.join();
        consumer.join();
    }
}
