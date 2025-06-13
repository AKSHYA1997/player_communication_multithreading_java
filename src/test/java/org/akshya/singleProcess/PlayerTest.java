package org.akshya.singleProcess;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    @Test
    void testPlayersExchangeTenMessagesAndStop() throws InterruptedException {
        Player player1 = new Player("Player1", true);
        Player player2 = new Player("Player2", false);

        player1.setOtherPlayer(player2);
        player2.setOtherPlayer(player1);

        Thread t1 = new Thread(player1);
        Thread t2 = new Thread(player2);

        t1.start();
        t2.start();

        t1.join(5000);
        t2.join(5000);

        assertEquals(19, player1.getMessageCount().get() + player2.getMessageCount().get(),
                "Total messages exchanged should be 19");
        assertTrue(player1.isStop(), "Player1 should have stopped");
        assertTrue(player2.isStop(), "Player2 should have stopped");
    }

    @Test
    void testStopTerminatesPlayerImmediately() throws InterruptedException {
        Player player = new Player("TestPlayer", false);
        player.stop();

        Thread thread = new Thread(player);
        thread.start();
        thread.join(1000);

        assertTrue(player.isStop(), "Player should remain stopped");
        assertEquals(0, player.getMessageCount().get(), "No messages should be processed");
    }

    @Test
    void testEnqueueMessageDoesNotThrow() {
        Player player = new Player("EnqueueTester", false);

        assertDoesNotThrow(() -> player.enqueueMessage("TestMessage"),
                "Enqueue should not throw exception");
    }
}
