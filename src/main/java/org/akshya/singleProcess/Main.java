package org.akshya.singleProcess;

import lombok.extern.log4j.Log4j;

@Log4j
public class Main {

    public static void main(String[] args) {
        Player player1 = new Player("Player1", true);
        Player player2 = new Player("Player2", false);
        player1.setOtherPlayer(player2);
        player2.setOtherPlayer(player1);

        Thread t1 = new Thread(player1);
        Thread t2 = new Thread(player2);

        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            log.error("Main thread interrupted: " + e.getMessage());
        }
    }
}