package org.akshya.sockets;

import lombok.extern.log4j.Log4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import static org.akshya.helper.Constants.*;

/**
 * The {@code ClientPlayer} class represents a client-side implementation of the player
 * in the multi-process communication system. It connects to a {@code ServerPlayer},
 * exchanges messages, and logs the conversation.
 *
 * <p>Responsibilities:</p>
 * <ul>
 *   <li>Establish a socket connection to the {@code ServerPlayer}.</li>
 *   <li>Send and receive messages in a loop until the stop condition is met.</li>
 *   <li>Maintain and update a counter to track the number of messages exchanged.</li>
 * </ul>
 */
@Log4j
public class ClientPlayer {

    public static void main(String[] args) {

        try (Socket socket = new Socket(HOST, PORT)) {
            log.info(String.format("************** Connected to %s (ServerPlayer) **************", PLAYER_2));

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            int messageCount = 0;
            String message = "Msg " + (messageCount + 1);
            while (messageCount < 10) {
                log.info(PLAYER_1 + " sent: " + message);
                out.println(message);

                String reply = in.readLine();
                if (reply == null) break;

                log.info(PLAYER_1 + " replied: " + reply);
                messageCount++;
                message = reply + " | Msg " + (messageCount + 1);
            }

            log.info(String.format("************** %s: Conversation complete. Closing connection. **************", PLAYER_1));
        } catch (IOException e) {
            log.error(PLAYER_1 + " error: " + e.getMessage());
        }
    }
}

