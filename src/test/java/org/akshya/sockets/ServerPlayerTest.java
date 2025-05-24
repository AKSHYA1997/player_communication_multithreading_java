package org.akshya.sockets;

import org.junit.jupiter.api.Test;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ServerPlayerTest {

    @Test
    void testServerPlayerCommunication() throws IOException {
        ServerSocket mockServerSocket = mock(ServerSocket.class);
        Socket mockSocket = mock(Socket.class);

        ByteArrayInputStream inputStream = new ByteArrayInputStream(
                ("Msg 1\nMsg 2\nMsg 3\nMsg 4\nMsg 5\nMsg 6\nMsg 7\nMsg 8\nMsg 9\nMsg 10\n").getBytes()
        );
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        when(mockServerSocket.accept()).thenReturn(mockSocket);
        when(mockSocket.getInputStream()).thenReturn(inputStream);
        when(mockSocket.getOutputStream()).thenReturn(outputStream);

        try (BufferedReader in = new BufferedReader(new InputStreamReader(mockSocket.getInputStream()));
             PrintWriter out = new PrintWriter(new OutputStreamWriter(mockSocket.getOutputStream()), true)) {

            int messageCount = 0;
            while (messageCount < 10) {
                String message = in.readLine();
                if (message == null) break;

                String reply = message + " | Reply " + (++messageCount);
                out.println(reply);
            }
        }

        String[] responses = outputStream.toString().split("\n");
        for (int i = 0; i < 10; i++) {
            assert responses[i].equals("Msg " + (i + 1) + " | Reply " + (i + 1));
        }
    }
}
