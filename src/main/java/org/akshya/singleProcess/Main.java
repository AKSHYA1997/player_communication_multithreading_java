package org.akshya.singleProcess;

import static org.akshya.helper.Constants.PLAYER_1;
import static org.akshya.helper.Constants.PLAYER_2;

public class Main {

    public static void main(String[] args) {
        Player player2 = new Player(PLAYER_2, null, false);
        Player player1 = new Player(PLAYER_1, player2, true);
        player2.setOtherPlayer(player1);

        Thread thread1 = new Thread(player1);
        Thread thread2 = new Thread(player2);

        thread1.start();
        thread2.start();
    }
}