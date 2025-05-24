package org.akshya.sockets;

import org.junit.jupiter.api.Test;

import java.io.*;
import java.net.Socket;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ClientPlayerTest {

    @Test
    void testClientPlayerCommunication() throws IOException {
        Socket mockSocket = mock(Socket.class);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(
                ("Reply 1\nReply 2\nReply 3\nReply 4\nReply 5\nReply 6\nReply 7\nReply 8\nReply 9\nReply 10\n").getBytes()
        );

        when(mockSocket.getInputStream()).thenReturn(inputStream);
        when(mockSocket.getOutputStream()).thenReturn(outputStream);

        PrintStream systemOut = System.out;
        ByteArrayOutputStream testOut = new ByteArrayOutputStream();
        System.setOut(new PrintStream(testOut));

        try (BufferedReader in = new BufferedReader(new InputStreamReader(mockSocket.getInputStream()));
             PrintWriter out = new PrintWriter(new OutputStreamWriter(mockSocket.getOutputStream()), true)) {

            int messageCount = 0;
            String message = "Msg " + (messageCount + 1);
            while (messageCount < 10) {
                System.out.println("Player 1 sent: " + message);
                out.println(message);

                String reply = in.readLine();
                if (reply == null) break;

                System.out.println("Player 1 received: " + reply);
                messageCount++;
                message = reply + " | Msg " + (messageCount + 1);
            }
        }

        String[] outputs = testOut.toString().split("\n");
        assert outputs[0].contains("Player 1 sent: Msg 1");
        assert outputs[1].contains("Player 1 received: Reply 1");

        System.setOut(systemOut);
    }
}
