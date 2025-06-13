package org.akshya.singleProcess;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * The {@code Player} class represents an entity that can communicate with another {@code Player} instance
 * in a conversational manner. It demonstrates concurrent message exchange, where each player sends and
 * receives messages in a loop until a specified stop condition is met.
 *
 * <p>Responsibilities:</p>
 * <ul>
 *   <li>Initiate and participate in a message exchange with another {@code Player} instance.</li>
 *   <li>Maintain and update a message counter to track the number of messages sent or received.</li>
 *   <li>Stop the communication after 10 messages have been sent and received.</li>
 * </ul>
 *
 * <p>This class uses a multi-threaded approach where each player runs in a separate thread to simulate
 * concurrent interactions.</p>
 */
@Getter
@Setter
@Log4j
public class Player implements Runnable {
    private final String name;
    private Player otherPlayer;
    private final AtomicInteger messageCount = new AtomicInteger(0);
    private volatile boolean isInitiator;
    private volatile boolean stop = false;
    private final MessageQueue queue = new MessageQueue();

    public Player(String name, boolean isInitiator) {
        this.name = name;
        this.isInitiator = isInitiator;
    }

    public void stop() {
        this.stop = true;
        synchronized (queue) {
            queue.notifyAll();
        }
    }

    @Override
    public void run() {
        try {
            if (isInitiator) {
                String message = "Msg " + messageCount.incrementAndGet();
                log.info(name + " sent: " + message);
                otherPlayer.enqueueMessage(message);
                isInitiator = false;
            }

            while (!stop) {
                String received = queue.take();
                if (received == null) continue;

                String newMessage;
                if (name.equals("Player1")) {
                    newMessage = received + " | Msg " + messageCount.incrementAndGet();
                    log.info(name + " sent: " + newMessage);
                } else {
                    newMessage = received + " | Reply " + messageCount.incrementAndGet();
                    log.info(name + " replied: " + newMessage);
                }

                if (messageCount.get() == 10) {
                    stop = true;
                    if (otherPlayer != null) otherPlayer.stop();
                    break;
                }
                otherPlayer.enqueueMessage(newMessage);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error(name + " interrupted.", e);
        }
    }

    public void enqueueMessage(String message) throws InterruptedException {
        queue.put(message);
    }
}

