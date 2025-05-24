package org.akshya.singleProcess;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

public class PlayerTest {

    private Player player1;
    private Player player2;

    @BeforeEach
    void setup() {
        player2 = mock(Player.class);
        player1 = new Player("Player 1", player2, true);
    }

    @Test
    void testSendMessage() {
        player1.sendMessage("Test Message");
        verify(player2, times(1)).receiveMessage("Test Message");
    }

    @Test
    void testReceiveMessageAndStop() {
        player1.receiveMessage("Msg 1");

        verify(player2, times(1)).receiveMessage(contains("Msg 1 | Msg 1"));

        for (int i = 2; i <= 10; i++) {
            player1.receiveMessage("Msg " + i);
        }
        assert player1.getMessageCount().get() == 10;
    }

    @Test
    void testStop() {
        player1.stop();
        assert player1.isStop();
    }
}
