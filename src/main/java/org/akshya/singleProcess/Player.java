package org.akshya.singleProcess;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j;

import java.util.concurrent.atomic.AtomicInteger;

import static org.akshya.helper.Constants.PLAYER_1;

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

    public Player(String name, Player otherPlayer, boolean isInitiator) {
        this.name = name;
        this.otherPlayer = otherPlayer;
        this.isInitiator = isInitiator;
    }

    public void stop() {
        this.stop = true;
    }

    @Override
    public void run() {
        while (!stop) {
            if (isInitiator) {
                String message = "Msg " + messageCount.incrementAndGet();
                sendMessage(message);
                isInitiator = false;
            }
        }
    }

    public synchronized void receiveMessage(String message) {
        if (stop) return;
        String newMessage;
        if (name.equals(PLAYER_1)) {
            newMessage = message + " | Msg " + (messageCount.incrementAndGet());
            log.info(name + " sent: " + newMessage);
        } else {
            newMessage = message + " | Reply " + (messageCount.incrementAndGet());
            log.info(name + " replied: " + newMessage);
        }

        if (messageCount.get() == 10) {
            stop = true;
            if (otherPlayer != null) otherPlayer.stop();
            return;
        }

        otherPlayer.receiveMessage(newMessage);
    }

    public synchronized void sendMessage(String message) {
        log.info(name + " sent: " + message);
        otherPlayer.receiveMessage(message);
    }
}

