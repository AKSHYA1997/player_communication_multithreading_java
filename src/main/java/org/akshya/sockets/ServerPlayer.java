package org.akshya.sockets;

import lombok.extern.log4j.Log4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import static org.akshya.helper.Constants.*;

/**
 * The {@code ServerPlayer} class represents a server-side implementation of the player
 * in the multi-process communication system. It waits for a connection from a {@code ClientPlayer},
 * exchanges messages, and logs the conversation.
 *
 * <p>Responsibilities:</p>
 * <ul>
 *   <li>Listen for a connection from the {@code ClientPlayer} using a {@code ServerSocket}.</li>
 *   <li>Receive and reply to messages in a loop until the stop condition is met.</li>
 *   <li>Maintain and update a counter to track the number of messages exchanged.</li>
 * </ul>
 */
@Log4j
public class ServerPlayer {
    public static void main(String[] args) {

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            log.info(String.format("************** %s (ServerPlayer) is waiting for connection **************", PLAYER_2));
            Socket socket = serverSocket.accept();
            log.info(String.format("************** %s (clientPlayer) connected **************", PLAYER_1));

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            int messageCount = 0;
            while (messageCount < 10) {
                String message = in.readLine();
                if (message == null) break;

                log.info(PLAYER_2 + " received: " + message);
                messageCount++;
                String reply = message + " | Reply " + messageCount;
                log.info(PLAYER_2 + " replied with: " + reply);
                out.println(reply);
            }

            log.info(String.format("************** %s: Conversation complete. Closing connection. **************", PLAYER_2));
        } catch (IOException e) {
            log.error(PLAYER_2 + " error: " + e.getMessage());
        }
    }
}
